package com.dolphin.api.directives

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.Credentials
import akka.http.scaladsl.server.{AuthorizationFailedRejection, Directive0, Directive1, ValidationRejection}
import com.dolphin.api.entity.{AuthResponse, UserJson}
import com.dolphin.components.Components
import com.dolphin.db.entity.User
import com.dolphin.utils.Logging

import scala.concurrent.Future
import scala.util.{Failure, Success}

trait CommonDirectives extends Logging {
  this: Components =>

  def authUser(userJson: UserJson): Directive1[AuthResponse] = {
    onComplete(components.authService.getUserByCredentials(userJson)).flatMap {
      case Success(Some(u)) => providerAuth(u)
      case Success(None) => reject(AuthorizationFailedRejection)
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

  def checkUser(userJson: UserJson): Directive0 = {
    onSuccess(components.authService.getUserByCredentials(userJson)).flatMap {
      case Some(_) => reject(ValidationRejection("User exists"))
      case None => pass
    }
  }

  def extractUserId(user: User): Directive1[Int] = {
    user.id match {
      case Some(id) => provide(id)
      case None => reject(AuthorizationFailedRejection)
    }
  }

  def getUserByToken(token: String): Directive1[User] = {
    onSuccess(components.tokenDao.getUserByToken(token)).flatMap {
      case Some(user) => provide(user)
      case None => reject(AuthorizationFailedRejection)
    }
  }

  private def providerAuth(user: User): Directive1[AuthResponse] = {
    user.id
      .map { id =>
        onSuccess(components.authService.getUserToken(id))
          .flatMap(token => provide(AuthResponse(token, user.toResponse)))
      }
      .getOrElse(reject(AuthorizationFailedRejection))

  }
}
