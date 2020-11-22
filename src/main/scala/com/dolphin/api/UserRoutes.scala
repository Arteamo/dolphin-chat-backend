package com.dolphin.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.dolphin.api.directives.CommonDirectives
import com.dolphin.api.entity.ImageUpdate
import com.dolphin.api.entity.ImageUpdate._
import com.dolphin.components.Components
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

trait UserRoutes extends CommonDirectives {
  this: Components =>

  private val changeImageRoute: Route = {
    (path("user" / "image") & post & authenticateOAuth2Async("dolphin", oauthUser) & entity(as[ImageUpdate])) {
      (user, update) =>
        extractUserId(user) { userId => complete(components.userDao.updateUserImage(userId, update.encodedImage)) }
    }
  }

  private val userInfoRoute: Route = {
    (path("user" / IntNumber) & get) { userId =>
      complete(components.userDao.getUserById(userId).map(_.map(_.toResponse)))
    }
  }

  val userRoutes: Route = changeImageRoute ~ userInfoRoute
}
