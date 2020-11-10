package com.dolphin.api
import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.dolphin.api.directives.CommonDirectives
import com.dolphin.components.Components
import com.dolphin.components.chat.ChatRoom

trait ChatRoutes extends CommonDirectives {
  this: Components =>
  private implicit lazy val actorSystem: ActorSystem = components.actorSystem
  private val chatWebsocketRoute: Route = {
    (path("ws" / "chats" / IntNumber) & get & parameter("name")) { (id, name) =>
      handleWebSocketMessages(ChatRoom.findOrCreate(id).websocketFlow(name))
    }
  }

  val chatRoutes: Route = chatWebsocketRoute
}
