package com.dolphin.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.dolphin.api.directives.CommonDirectives
import com.dolphin.api.entity.ImageUpdate
import com.dolphin.api.entity.RoomJson._
import com.dolphin.components.Components
import com.dolphin.db.entity.UserToRoom._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport.{marshaller, unmarshaller}

import scala.util.Try

trait RoomRoutes extends CommonDirectives {
  this: Components =>

  private val listUserRoomsRoute: Route = {
    (path("rooms" / "list") & get & authenticateOAuth2Async("dolphin", oauthUser)) { user =>
      extractUserId(user) { userId => complete(components.roomService.listRoomsByUserWithLastMessage(userId)) }
    }
  }

  private val listUsersInRoomRoute: Route = {
    (path("rooms" / "list" / "users" / IntNumber) & get) { roomId =>
      complete(components.userToRoomDao.listUsersByRoomId(roomId))
    }
  }

  private val joinRoomRoute: Route = {
    (path("rooms" / "join" / IntNumber) & put & authenticateOAuth2Async("dolphin", oauthUser)) { (roomId, user) =>
      extractUserId(user) { userId => complete(components.userToRoomDao.createRelation(userId, roomId)) }
    }
  }

  private val leaveRoomRoute: Route = {
    (path("rooms" / "leave" / IntNumber) & put & authenticateOAuth2Async("dolphin", oauthUser)) { (roomId, user) =>
      extractUserId(user) { userId => complete(components.userToRoomDao.removeRelation(userId, roomId)) }
    }
  }

  private val updateRoomImageRoute: Route = {
    (path("rooms" / "image" / IntNumber) & post & entity(as[ImageUpdate])) { (roomId, imageUpdate) =>
      complete(components.roomDao.updateRoomImage(roomId, imageUpdate))
    }
  }

  private val updateRoomTitleRoute: Route = {
    (path("rooms" / "title" / IntNumber) & post & parameter("title")) { (roomId, title) =>
      complete(components.roomDao.updateRoomTitle(roomId, title))
    }
  }

  private val listMessageWithPaginationRoute: Route = {
    (path("room" / IntNumber / "messages") & get & parameter("page")) { (roomId, page) =>
      complete(components.messageDao.listMessagesWithPagination(roomId, Try(page.toInt).getOrElse(1)))
    }
  }

  val roomRoutes: Route =
    listUserRoomsRoute ~
      listUsersInRoomRoute ~
      joinRoomRoute ~
      leaveRoomRoute ~
      updateRoomImageRoute ~
      updateRoomTitleRoute ~
      listMessageWithPaginationRoute
}
