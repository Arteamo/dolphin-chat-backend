package com.dolphin.db.entity

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import slick.jdbc.PostgresProfile.api._
import slick.lifted.{ProvenShape, Tag}

case class Room(
  id: Option[Int] = None,
  title: String,
  encodedImage: Option[String] = None
)

object Room {
  implicit val roomEncoder: Encoder[Room] = deriveEncoder[Room]

  implicit val roomDecoder: Decoder[Room] = deriveDecoder[Room]
}

class RoomTable(tag: Tag) extends Table[Room](tag, "rooms") {
  override def * : ProvenShape[Room] = (id.?, title, encodedImage.?) <> ((Room.apply _).tupled, Room.unapply)

  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def title: Rep[String] = column[String]("title")

  def encodedImage: Rep[String] = column[String]("encoded_image")

}

object RoomTable {
  val RoomTable = TableQuery[RoomTable]
}
