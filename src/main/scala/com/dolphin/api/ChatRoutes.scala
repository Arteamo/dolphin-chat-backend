package com.dolphin.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.dolphin.api.directives.CommonDirectives
import com.dolphin.components.chat.ChatRoom
import com.dolphin.components.{Components, ComponentsHolder}
import com.dolphin.db.entity.Room
import com.dolphin.utils.Logging
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport.{marshaller, unmarshaller}

trait ChatRoutes extends CommonDirectives with Logging {
  this: Components =>
  private implicit lazy val componentsHolder: ComponentsHolder = components

  private val chatWebsocketRoute: Route = {
    (path("ws" / "chats" / IntNumber) & get & parameter("access_token")) { (roomId, token) =>
      getUserByToken(token) { user =>
        extractUserId(user) { userId => handleWebSocketMessages(ChatRoom.findOrCreate(roomId).websocketFlow(userId)) }
      }
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
