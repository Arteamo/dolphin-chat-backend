package com.dolphin.api.entity

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}

case class UserJson(username: Option[String], password: String, email: String)

object UserJson {
  implicit val userJsonEncoder: Encoder[UserJson] = deriveEncoder[UserJson]

  implicit val userJsonDecoder: Decoder[UserJson] = deriveDecoder[UserJson]
}
