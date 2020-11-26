package com.dolphin.api.entity

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class UserUpdate(username: Option[String] = None, password: Option[String] = None, image: Option[ImageUpdate])

object UserUpdate {
  implicit val userUpdateEncoder: Encoder[UserUpdate] = deriveEncoder[UserUpdate]

  implicit val userUpdateDecoder: Decoder[UserUpdate] = deriveDecoder[UserUpdate]
}
