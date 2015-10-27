package com.labs2160.slacker.plugin.poller

import java.util.Properties

import com.labs2160.slacker.api.annotation.ActionDescription
import com.labs2160.slacker.api.{Action, SlackerContext}
import com.typesafe.scalalogging.LazyLogging

/**
 * Arguments: *NONE*
 */
@ActionDescription(
    name = "List Polls",
    description = "Displays open and closed polls"
)
class ListPollsAction extends Action with LazyLogging {

    override def setConfiguration(properties: Properties): Unit = {}

    override def execute(slackerContext: SlackerContext): Boolean = {
        logger.info("List polls:")
        val msg = if (pollRegistry.isEmpty) {
            "No polls at the moment.  Why don't you create one?"
        } else {
            pollRegistry.values.map(p => s"${p.name} - ${p.description}${if (!p.isOpen) s" (CLOSED on ${format(p.closed)})" else ""}").mkString("\n")
        }
        logger.info(msg)
        slackerContext.setResponseMessage(msg);
        true
    }
}
