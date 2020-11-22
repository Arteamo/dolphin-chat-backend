package com.dolphin.db.dao

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

  def updateUserImage(userId: Int, encodedImage: String): Future[Int]
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

  override def updateUserImage(userId: Int, encodedImage: String): Future[Int] = {
    val query = UserTable.filter(_.id === userId).map(_.encodedImage).update(encodedImage)
    db.run(query.transactionally)
  }
}
