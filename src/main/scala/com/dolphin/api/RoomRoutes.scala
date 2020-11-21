package com.dolphin.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.dolphin.api.directives.CommonDirectives
import com.dolphin.api.entity.RoomJson._
import com.dolphin.components.Components
import com.dolphin.db.entity.UserToRoom._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

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

  val roomRoutes: Route = listUserRoomsRoute ~ listUsersInRoomRoute ~ joinRoomRoute ~ leaveRoomRoute
}
