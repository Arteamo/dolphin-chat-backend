package com.dolphin.db.entity

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import slick.jdbc.PostgresProfile.api._
import slick.lifted.{ProvenShape, Tag}

case class UserToRoom(
  userId: Int,
  roomId: Int
)

object UserToRoom {
  implicit val userToRoomEncoder: Encoder[UserToRoom] = deriveEncoder[UserToRoom]

  implicit val userToRoomDecoder: Decoder[UserToRoom] = deriveDecoder[UserToRoom]
}

class UserToRoomTable(tag: Tag) extends Table[UserToRoom](tag, "users_to_rooms") {
  override def * : ProvenShape[UserToRoom] = (userId, roomId) <> ((UserToRoom.apply _).tupled, UserToRoom.unapply)

  def userId: Rep[Int] = column[Int]("user_id")

  def roomId: Rep[Int] = column[Int]("room_id")
}

object UserToRoomTable {
  val UserToRoomTable = TableQuery[UserToRoomTable]
}
