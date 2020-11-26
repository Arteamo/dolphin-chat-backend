package com.dolphin.components.auth

import com.dolphin.api.entity.UserJson
import com.dolphin.db.dao.{TokenDao, UserDao}
import com.dolphin.db.entity.{Token, User}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.hashing.MurmurHash3

class AuthService(userDao: UserDao, tokenDao: TokenDao)(implicit ec: ExecutionContext) {
  import AuthService._

  def createUser(userJson: UserJson): Future[String] = {
    userDao.create(
      User(
        username = userJson.username.getOrElse(throw new RuntimeException("Can't create user without name")),
        email = userJson.email,
        passwordHash = hashForPassword(userJson)
      )
    )
  }.map(_ => "OK")

  def getUserByCredentials(userJson: UserJson): Future[Option[User]] = {
    userDao.getByEmail(userJson.email).map {
      case user @ _ => user.filter(_.passwordHash == hashForPassword(userJson))
    }
  }

  def getUserByToken(token: String): Future[Option[User]] = {
    userDao.getByToken(token)
  }

  def getUserToken(userId: Int): Future[Token] = {
    tokenDao.getByUserId(userId).flatMap {
      case Some(token) => Future.successful(token)
      case None => tokenDao.createToken(userId)
    }
  }

  def logout(userId: Int): Future[(String, Int)] = {
    tokenDao.deleteByUserId(userId).map("status" -> _)
  }
}

object AuthService {

  def hashForPassword(userJson: UserJson): String = {
    hashForPassword(userJson.password)
  }

  def hashForPassword(password: String): String = {
    MurmurHash3.stringHash(password).toString
  }
}
