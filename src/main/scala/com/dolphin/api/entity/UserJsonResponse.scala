package com.dolphin.api.entity

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class UserJsonResponse(
  id: Option[Int] = None,
  username: String,
  email: String,
  encodedImage: Option[String] = None
)

object UserJsonResponse {
  implicit val userJsonResponseEncoder: Encoder[UserJsonResponse] = deriveEncoder[UserJsonResponse]

  implicit val userJsonResponseDecoder: Decoder[UserJsonResponse] = deriveDecoder[UserJsonResponse]
}
