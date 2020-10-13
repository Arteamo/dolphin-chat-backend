package com.dolphin.db.dao

import java.util.UUID

import com.dolphin.db.entity.Token
import com.dolphin.db.entity.TokenTable.TokenTable
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

trait TokenDao {
  def getByUserId(userId: Int): Future[Option[Token]]

  def createToken(userId: Int): Future[Token]
}

class TokenDaoImpl(db: Database)(implicit ec: ExecutionContext) extends TokenDao {

  override def getByUserId(userId: Int): Future[Option[Token]] = {
    val query = TokenTable.filter(_.userId === userId).result
    db.run(query.headOption)
  }

  override def createToken(userId: Int): Future[Token] = {
    val query = (TokenTable returning TokenTable.map(_.token)) into ((token, tokenValue) =>
      token.copy(token = tokenValue)
    ) += Token(UUID.randomUUID().toString, userId)
    db.run(query.transactionally)
  }
}
