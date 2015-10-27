package com.labs2160.slacker.plugin.poller

import java.util.Properties

import com.labs2160.slacker.api.annotation.ActionDescription
import com.labs2160.slacker.api.{Action, NoArgumentsFoundException, SlackerContext}

@ActionDescription(
    name = "Poll Add-Option",
    description = "Adds an option to an existing poll",
    argsSpec = "<pollname> <choice> <description...>",
    argsExample = "myFruitPoll a Apple"
)
class AddOptionAction extends Action {

     override def setConfiguration(properties: Properties): Unit = {

     }

     override def execute(slackerContext: SlackerContext): Boolean = {
         val args = slackerContext.getRequestArgs
         if (args == null || args.length < 3) {
             throw new NoArgumentsFoundException("Must supply arguments: <pollname> <choice> <description>");
         }

         // just for readability
         val pollName = args(0)
         val choice = args(1)
         val description = args.slice(2, args.length).mkString(" ")

         val poll = pollRegistry.getOrElse(pollName,
             throw new NoArgumentsFoundException(s"""Poll with name "${pollName}" not found"""))
         poll.addOption(choice, description)

         slackerContext.setResponseMessage(s"""Option "${choice}) ${description}" added to poll "${pollName}".""")
         true
     }
 }
