package com.dolphin.api.entity

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class ImageUpdate(encodedImage: String, setDefaultImage: Boolean = false)

object ImageUpdate {
  implicit val imageUpdateEncoder: Encoder[ImageUpdate] = deriveEncoder[ImageUpdate]

  implicit val imageUpdateDecoder: Decoder[ImageUpdate] = deriveDecoder[ImageUpdate]
}
