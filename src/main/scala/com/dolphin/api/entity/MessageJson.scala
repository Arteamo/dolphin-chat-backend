package com.dolphin.api.entity

import java.sql.Timestamp

import com.dolphin.db.entity.Message
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

final case class MessageJson(
  text: String,
  messageType: String,
  sendTimestamp: Long,
  encodedData: Option[String] = None
) {

  def toMessage(senderId: Int, roomId: Int): Message = {
    Message(
      messageText = text,
      messageType = messageType,
      sendTimestamp = new Timestamp(sendTimestamp),
      senderId = senderId,
      roomId = roomId,
      encodedData = encodedData
    )
  }
}

object MessageJson {
  implicit val chatMessageEncoder: Encoder[MessageJson] = deriveEncoder[MessageJson]

  implicit val chatMessageDecoder: Decoder[MessageJson] = deriveDecoder[MessageJson]
}
