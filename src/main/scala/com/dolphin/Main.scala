package com.dolphin

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import com.dolphin.api.Routes
import com.dolphin.components.{Components, ComponentsHolder}
import com.dolphin.utils.Logging
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContextExecutor

object Main extends App with Components with Routes with Logging {
  implicit lazy val system: ActorSystem = ActorSystem()
  override implicit lazy val ec: ExecutionContextExecutor = system.dispatcher

  val config = ConfigFactory.load()
  val host = config.getString("http.host")
  val port = config.getInt("http.port")

  override lazy val components = ComponentsHolder.create(system)

  Http().newServerAt(host, port).bind(routes)
  log.info(s"Application started on http://$host:$port")
}
