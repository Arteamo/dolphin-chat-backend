package com.dolphin.db.dao

import com.dolphin.api.entity.{ImageUpdate, UserUpdate}
import com.dolphin.components.auth.AuthService
import com.dolphin.db.entity.TokenTable.TokenTable
import com.dolphin.db.entity.User
import com.dolphin.db.entity.UserTable.UserTable
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

trait UserDao {
  def create(user: User): Future[Int]

  def getByEmail(email: String): Future[Option[User]]

  def getByToken(token: String): Future[Option[User]]

  def getUserById(id: Int): Future[Option[User]]

  def userUpdate(userId: Int, update: UserUpdate): Future[Int]
}

class UserDaoImpl(db: Database)(implicit ec: ExecutionContext) extends UserDao {

  override def create(user: User): Future[Int] = {
    val query = UserTable += user
    db.run(query.transactionally)
  }

  override def getByEmail(email: String): Future[Option[User]] = {
    val query = UserTable.filter(_.email === email)
    db.run(query.result.headOption)
  }

  override def getByToken(token: String): Future[Option[User]] = {
    val query = (UserTable join TokenTable on (_.id === _.userId))
      .filter { case (_, tt) => tt.token === token }
    db.run(query.result.headOption.transactionally).map(_.map(_._1))
  }

  override def getUserById(id: Int): Future[Option[User]] = {
    val query = UserTable.filter(_.id === id)
    db.run(query.result.headOption)
  }

  override def userUpdate(userId: Int, update: UserUpdate): Future[Int] = {
    val query = UserTable
      .filter(_.id === userId)
      .map(u => (u.email, u.username, u.passwordHash, u.encodedImage))

    getUserById(userId).flatMap { userOpt =>
      val user = userOpt.getOrElse(throw new RuntimeException(s"Cannot update user $userId"))

      val queryWithUpdate = query
        .update(
          (
            update.email.getOrElse(user.email),
            update.username.getOrElse(user.username),
            update.password.map(AuthService.hashForPassword).getOrElse(user.passwordHash),
            update.image match {
              case Some(ImageUpdate(Some(encImage), false)) => encImage
              case Some(ImageUpdate(_, true)) => null
              case _ => user.encodedImage.orNull
            }
          )
        )
      db.run(queryWithUpdate)
    }

  }
}
