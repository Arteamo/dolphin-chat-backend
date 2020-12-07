package com.dolphin.db.dao

import com.dolphin.api.entity.MessageJson
import com.dolphin.db.entity.{Message, User}
import com.dolphin.db.entity.MessageTable.MessageTable
import com.dolphin.db.entity.UserTable.UserTable
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

trait MessageDao {
  def create(message: Message): Future[Int]

  def lastMessage(roomId: Int): Future[Option[(Message, Option[User])]]

  def listMessagesWithPagination(roomId: Int, page: Int): Future[Seq[MessageJson]]
}

class MessageDaoImpl(db: Database, pageSize: Int)(implicit ec: ExecutionContext) extends MessageDao {

  override def create(message: Message): Future[Int] = {
    val query = MessageTable += message
    db.run(query.transactionally)
  }

  override def lastMessage(roomId: Int): Future[Option[(Message, Option[User])]] = {
    val messageQuery = MessageTable
      .filter(_.roomId === roomId)
      .sorted(_.sendTimestamp.desc)
    val userQuery = (msg: Message) =>
      UserTable
        .filter(_.id === msg.senderId)
    db.run(messageQuery.result.headOption)
      .flatMap(msgOpt =>
        msgOpt
          .map(msg => db.run(userQuery(msg).result.headOption))
          .getOrElse(Future.successful(None))
          .map(user => msgOpt.map(msg => (msg, user)))
      )
  }

  override def listMessagesWithPagination(roomId: Int, page: Int): Future[Seq[MessageJson]] = {
    val query = MessageTable.filter(_.roomId === roomId).drop((page - 1) * pageSize).take(pageSize)
    db.run(query.result).map(_.map(_.toMessageJson))
  }
}
