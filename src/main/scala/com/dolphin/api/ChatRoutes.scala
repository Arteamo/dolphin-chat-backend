package com.dolphin.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.dolphin.components.Components

trait ChatRoutes {
  this: Components =>

  private val echoRoute: Route = {
    (pathPrefix("chat") & get) {
      handleWebSocketMessages(components.chatService.echo)
    }
  }

  val chatRoutes: Route = echoRoute
}
