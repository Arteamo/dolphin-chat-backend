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
      .filter { case (ut, tt) => ut.id === tt.userId }
    db.run(query.result.headOption.transactionally).map(_.map(_._1))
  }
}
