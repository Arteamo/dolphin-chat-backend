package com.dolphin.api.entity

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class RoomUserCountJson(roomId: Int, userCount: Int)

object RoomUserCountJson {
  implicit val roomUserCountJsonEncoder: Encoder[RoomUserCountJson] = deriveEncoder[RoomUserCountJson]

  implicit val roomUserCountJsonDecoder: Decoder[RoomUserCountJson] = deriveDecoder[RoomUserCountJson]
}
