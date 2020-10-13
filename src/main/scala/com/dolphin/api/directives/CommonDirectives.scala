package com.dolphin.api.directives

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.Credentials
import akka.http.scaladsl.server.{AuthorizationFailedRejection, Directive1}
import com.dolphin.api.entity.UserJson
import com.dolphin.components.Components
import com.dolphin.db.entity.{Token, User}
import com.dolphin.utils.Logging

import scala.concurrent.Future
import scala.util.{Failure, Success}

trait CommonDirectives extends Logging {
  this: Components =>

  def authUser(userJson: UserJson): Directive1[Token] = {
    onComplete(components.authService.getUserByCredentials(userJson)).flatMap {
      case Success(Some(u)) => provideToken(u)
      case Success(None) => reject(AuthorizationFailedRejection) // TODO: Change rejection
      case Failure(e) =>
        log.error("Error occurred during auth", e)
        reject(AuthorizationFailedRejection)
    }
  }

  def oauthUser(credentials: Credentials): Future[Option[User]] = {
    credentials match {
      case Credentials.Provided(token) =>
        log.info(s"Token: $token")
        components.authService.getUserByToken(token)
      case _ =>
        log.info(s"No token")
        Future.successful(None)
    }
  }

  private def provideToken(user: User): Directive1[Token] = {
    user.id
        .map { id =>
          onSuccess(components.authService.getUserToken(id))
              .flatMap(token => provide(token))
        }
        .getOrElse(reject(AuthorizationFailedRejection))

  }
}
