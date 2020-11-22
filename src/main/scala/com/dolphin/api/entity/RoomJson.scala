package com.dolphin.api.entity

import com.dolphin.db.entity.Room
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class RoomJson(room: Room, lastMessage: Option[LastMessageJson] = None)

object RoomJson {
  implicit val encoder: Encoder[RoomJson] = deriveEncoder[RoomJson]

  implicit val decoder: Decoder[RoomJson] = deriveDecoder[RoomJson]
}
