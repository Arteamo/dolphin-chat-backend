package com.dolphin.api

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.dolphin.api.directives.CommonDirectives
import com.dolphin.components.Components
import com.dolphin.components.chat.ChatRoom
import com.dolphin.db.entity.Room
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport.{marshaller, unmarshaller}

trait ChatRoutes extends CommonDirectives {
  this: Components =>
  private implicit lazy val actorSystem: ActorSystem = components.actorSystem

  private val chatWebsocketRoute: Route = {
    (path("ws" / "chats" / IntNumber) & get & authenticateOAuth2Async("dolphin", oauthUser)) { (id, user) =>
      handleWebSocketMessages(ChatRoom.findOrCreate(id).websocketFlow(user.username))
    }
  }

  private val createRoomRoute: Route = {
    (path("chats" / "create") & post & entity(as[Room])) { room => complete(components.roomDao.create(room)) }
  }

  private val listRoomsRoute: Route = {
    (path("chats" / "list") & get) {
      complete(components.roomDao.list())
    }
  }

  private val findRoomByIdRoute: Route = {
    (path("chats" / "find" / IntNumber) & get) { id => complete(components.roomDao.findById(id)) }
  }

  private val findRoomByTitleRoute: Route = {
    (path("chats" / "find") & get & parameter("title")) { title => complete(components.roomDao.findByTitle(title)) }
  }

  val chatRoutes: Route =
    chatWebsocketRoute ~ createRoomRoute ~ listRoomsRoute ~ findRoomByIdRoute ~ findRoomByTitleRoute
}
