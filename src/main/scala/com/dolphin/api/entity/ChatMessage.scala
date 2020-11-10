package com.dolphin.api.entity

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

final case class ChatMessage(roomId: Int, senderId: Int, payload: String)

object ChatMessage {
  implicit val chatMessageEncoder: Encoder[ChatMessage] = deriveEncoder[ChatMessage]

  implicit val chatMessageDecoder: Decoder[ChatMessage] = deriveDecoder[ChatMessage]
}
