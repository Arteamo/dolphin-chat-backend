package com.dolphin.api.entity

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class LastMessageJson(message: MessageJson, sender: Option[UserJsonResponse] = None)

object LastMessageJson {
  implicit val lastMessageEncoder: Encoder[LastMessageJson] = deriveEncoder[LastMessageJson]

  implicit val lastMessageDecoder: Decoder[LastMessageJson] = deriveDecoder[LastMessageJson]
}
