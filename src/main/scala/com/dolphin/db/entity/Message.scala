package com.dolphin.db.entity

import java.sql.Timestamp

import com.dolphin.api.entity.MessageJson
import slick.jdbc.PostgresProfile.api._
import slick.lifted.{ProvenShape, Tag}

case class Message(
  id: Option[Int] = None,
  messageText: String,
  messageType: String,
  sendTimestamp: Timestamp,
  senderId: Int,
  roomId: Int,
  encodedData: Option[String] = None
) {

  def toMessageJson: MessageJson =
    MessageJson(messageText, messageType, sendTimestamp.getTime, encodedData, Some(senderId))
}

class MessageTable(tag: Tag) extends Table[Message](tag, "messages") {

  override def * : ProvenShape[Message] =
    (id.?, messageText, messageType, sendTimestamp, senderId, roomId, encodedData.?) <> (Message.tupled, Message.unapply)

  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def messageText: Rep[String] = column[String]("msg_text")

  def messageType: Rep[String] = column[String]("msg_type")

  def sendTimestamp: Rep[Timestamp] = column[Timestamp]("send_timestamp")

  def senderId: Rep[Int] = column[Int]("sender_id")

  def roomId: Rep[Int] = column[Int]("room_id")

  def encodedData: Rep[String] = column[String]("encoded_data")
}

object MessageTable {
  val MessageTable = TableQuery[MessageTable]
}
