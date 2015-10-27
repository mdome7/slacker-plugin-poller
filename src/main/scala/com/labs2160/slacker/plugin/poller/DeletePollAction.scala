package com.labs2160.slacker.plugin.poller

import java.util.Properties

import com.labs2160.slacker.api.annotation.ActionDescription
import com.labs2160.slacker.api.{Action, NoArgumentsFoundException, SlackerContext}

@ActionDescription(
    name = "Close Poll",
    description = "Close poll",
    argsSpec = "<pollName>",
    argsExample = "myFruitPoll"
)
class DeletePollAction extends Action {

     override def setConfiguration(properties: Properties): Unit = {

     }

     override def execute(slackerContext: SlackerContext): Boolean = {
         val args = slackerContext.getRequestArgs
         if (args == null || args.length < 1) {
             throw new NoArgumentsFoundException("Must supply arguments: <pollName>");
         }

         // just for readability
         val pollName = args(0)

         val poll = pollRegistry.getOrElse(pollName,
             throw new NoArgumentsFoundException(s"""Poll with name "${pollName}" not found"""))

         if (poll.isOpen) {
             slackerContext.setResponseMessage(s"""Poll "${pollName}" is still open, please close it first""")
         } else {
             pollRegistry.remove(pollName)
             slackerContext.setResponseMessage(s"""Poll "${pollName}" is now deleted\n${poll.toString}""")
         }
         true
     }
 }
