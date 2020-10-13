package com.dolphin.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.dolphin.components.Components

trait Routes extends AuthRoutes with ChatRoutes {
  this: Components =>

  def routes: Route = authRoutes ~ chatRoutes
}
