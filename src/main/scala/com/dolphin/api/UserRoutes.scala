package com.dolphin.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.dolphin.api.directives.CommonDirectives
import com.dolphin.api.entity.{ImageUpdate, UserUpdate}
import com.dolphin.api.entity.UserUpdate._
import com.dolphin.components.Components
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

trait UserRoutes extends CommonDirectives {
  this: Components =>

  private val updateUserRoute: Route = {
    (path("user" / "update") & post & authenticateOAuth2Async("dolphin", oauthUser) & entity(as[UserUpdate])) {
      (user, update) =>
        extractUserId(user) { userId => complete(components.userDao.userUpdate(userId, update)) }
    }
  }

  private val userInfoRoute: Route = {
    (path("user" / IntNumber) & get) { userId =>
      complete(components.userDao.getUserById(userId).map(_.map(_.toResponse)))
    }
  }

  val userRoutes: Route = updateUserRoute ~ userInfoRoute
}
