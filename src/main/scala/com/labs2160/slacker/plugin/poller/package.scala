package com.labs2160.slacker.plugin

import java.time.LocalDateTime

import scala.collection.mutable

/**
 * Created by mdometita on 10/27/15.
 */
package object poller {

    val pollRegistry = mutable.HashMap.empty[String,Poll]

    def format(dt: LocalDateTime) = dt.formatted("yyyy-MM-dd HH:mm:ss")
}
