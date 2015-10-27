package com.labs2160.slacker.plugin.poller

import java.util.Properties

import com.labs2160.slacker.api.annotation.ActionDescription
import com.labs2160.slacker.api.{NoArgumentsFoundException, SlackerContext, Action}
import com.typesafe.scalalogging.LazyLogging

/**
 * Arguments: {pollname} {password} {description}
 */
@ActionDescription(
    name = "Poll Create",
    description = "Creates a new poll with the given name and description",
    argsSpec = "<pollName> <description...>",
    argsExample = "myFruitPoll What is your favorite fruit?"
)
class CreatePollAction extends Action with LazyLogging {

    override def setConfiguration(properties: Properties): Unit = {

    }

    override def execute(slackerContext: SlackerContext): Boolean = {
        val args = slackerContext.getRequestArgs
        if (args == null || args.length < 2) {
            throw new NoArgumentsFoundException("Must supply arguments: <name> <description>");
        }

        val name = args(0)
        val description = args.slice(1, args.length).mkString(" ")
        pollRegistry.put(name, new Poll(name, description))

        slackerContext.setResponseMessage(s"""Poll "${name}" created. Please add options.""")
        true
    }
}
