package com.dolphin.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.dolphin.api.directives.CommonDirectives
import com.dolphin.api.entity.UserJson
import com.dolphin.api.entity.UserJson._
import com.dolphin.components.Components
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

trait AuthRoutes extends CommonDirectives {
  this: Components =>

  val registerRoute: Route = {
    (pathPrefix("register") & post & entity(as[UserJson])) { user =>
      checkUser(user) { complete(components.authService.createUser(user)) }
    }
  }

  val authRoute: Route = {
    (pathPrefix("auth") & post & entity(as[UserJson])) { userJson => authUser(userJson) { token => complete(token) } }
  }

  val logoutRoute: Route = {
    (pathPrefix("logout") & post & authenticateOAuth2Async("dolphin", oauthUser)) { user =>
      extractUserId(user) { userId => complete(components.authService.logout(userId)) }
    }
  }

  val authRoutes: Route = registerRoute ~ authRoute ~ logoutRoute
}
