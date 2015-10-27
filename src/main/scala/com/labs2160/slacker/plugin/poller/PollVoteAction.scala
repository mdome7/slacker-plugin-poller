package com.labs2160.slacker.plugin.poller

import java.util.Properties

import com.labs2160.slacker.api.annotation.ActionDescription
import com.labs2160.slacker.api.{Action, NoArgumentsFoundException, SlackerContext}

@ActionDescription(
    name = "Poll Vote",
    description = "Vote on a poll",
    argsSpec = "<pollName> <choice>",
    argsExample = "myFruitPoll a"
)
class PollVoteAction extends Action {

     override def setConfiguration(properties: Properties): Unit = {

     }

     override def execute(slackerContext: SlackerContext): Boolean = {
         val args = slackerContext.getRequestArgs
         if (args == null || args.length < 2) {
             throw new NoArgumentsFoundException("Must supply arguments: <pollName>");
         }

         // just for readability
         val pollName = args(0)
         val choice = args(1)

         val poll = pollRegistry.getOrElse(pollName,
             throw new NoArgumentsFoundException(s"""Poll with name "${pollName}" not found"""))

         if (poll.isOpen) {

             val description = poll.getChoiceLabel(choice)

             poll.vote(s"user${System.currentTimeMillis()}", choice)

             slackerContext.setResponseMessage(s"You voted on ${choice}) ${description}")
         } else {
             slackerContext.setResponseMessage(s"""Sorry, poll "${pollName}" was closed on ${poll.closed}""")
         }
         poll.isOpen
     }
 }
