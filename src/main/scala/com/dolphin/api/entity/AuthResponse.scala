package com.dolphin.api.entity

import com.dolphin.db.entity.Token
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class AuthResponse(token: Token, user: UserJsonResponse)

object AuthResponse {
  implicit val authResponseEncoder: Encoder[AuthResponse] = deriveEncoder[AuthResponse]

  implicit val authResponseDecoder: Decoder[AuthResponse] = deriveDecoder[AuthResponse]
}
