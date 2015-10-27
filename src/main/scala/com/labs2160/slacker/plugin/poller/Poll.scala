package com.labs2160.slacker.plugin.poller

import java.time.LocalDateTime

import com.labs2160.slacker.api.{Action, SlackerContext}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * IMPORTANT: THIS IS NOT THEAD-SAFE!
 * TODO: Make this thread-safe
 */
class Poll(val name: String, var description: String) {

    val opened = LocalDateTime.now

    var closed: LocalDateTime = _

    /** this sucks we can't rely on Vote.voter */
    val votes = mutable.HashMap.empty[String,Vote]

    val options = mutable.HashMap.empty[String,String]

    def isOpen() = closed == null

    def close(): Unit = {
        closed = LocalDateTime.now
    }

    def addOption(choice: String, label: String): Unit = {
        options.put(choice, label)
    }

    /**
     * Removes an existing choice add return any existing votes if any
     * @param choice
     * @return
     */
    def removeOption(choice: String): Seq[Vote] = {
        getChoiceLabel(choice) // validate choice
        val origVotes = getVotes(choice)
        origVotes.foreach(v => votes.remove(v.voter))
        origVotes
    }

    /**
     * Get votes for a specific choice
     * @param choice
     * @return
     */
    def getVotes(choice: String): Seq[Vote] = {
        getChoiceLabel(choice) // validate choice
        votes.values.filter(v => v.choice.equals(choice)).toSeq
    }

    def getVoteCounts(): Map[String,Int] = {
        votes.values.map(_.choice).groupBy(identity).mapValues(_.size)
    }

    def getWinningChoices(): Seq[String] = {
        if (isOpen || votes.isEmpty) {
            Seq.empty[String]
        } else {
            val winners = ListBuffer.empty[String]
            val counts = getVoteCounts.toList.sortWith(_._2 > _._2)
            var maxCount = counts(0)._2

            counts.iterator.filter(_._2 == maxCount).map(_._1).toSeq
        }
    }

    def getChoiceLabel(choice: String): String = {
        options.getOrElse(choice, throw new IllegalArgumentException(s"Not a valid choice: ${choice}"))
    }

    def vote(voter: String, choice: String): Option[Vote] = {
        getChoiceLabel(choice) // validate choice
        val originalVote = votes.get(voter)
        votes.put(voter, Vote(voter, choice))
        originalVote
    }

    override def toString(): String = {
        val msg = new StringBuilder(s"${this.description}\n")
        if (!this.isOpen) {
            msg ++= s"*** CLOSED on ${format(this.closed)} ***\n"
        }
        val winners = this.getWinningChoices
        if (winners.nonEmpty) {
            msg ++= (if (winners.length > 1) "!!! Winners: " else "!!! Winner: ")
            msg ++= s"${winners.mkString(", ")} !!!"
        }
        msg ++= f"# Votes     Choice\n"

        val counts = this.getVoteCounts()
        this.options.foreach {case (choice, label) =>
            val count = counts.getOrElse(choice, 0)
            msg ++= f"$count%7d$choice%10s) ${label}\n"
        }
        msg.toString
    }
}

case class Vote(voter: String, choice: String, timestamp: LocalDateTime = LocalDateTime.now)
