package com.dolphin.db.entity

import java.sql.Timestamp

import com.dolphin.api.entity.UserJsonResponse
import slick.jdbc.PostgresProfile.api._
import slick.lifted.{ProvenShape, TableQuery, Tag}

case class User(
  id: Option[Int] = None,
  username: String,
  passwordHash: String,
  email: String,
  createdTimestamp: Timestamp = new Timestamp(System.currentTimeMillis()),
  encodedImage: Option[String] = None
) {
  def toResponse: UserJsonResponse = UserJsonResponse(username, email)
}

class UserTable(tag: Tag) extends Table[User](tag, "users") {

  override def * : ProvenShape[User] =
    (id.?, username, passwordHash, email, createdTimestamp, encodedImage.?) <> (User.tupled, User.unapply)

  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def username: Rep[String] = column[String]("username", O.Unique)

  def passwordHash: Rep[String] = column[String]("password_hash")

  def email: Rep[String] = column[String]("email")

  def createdTimestamp: Rep[Timestamp] = column[Timestamp]("created_timestamp")

  def encodedImage: Rep[String] = column[String]("encoded_image")
}

object UserTable {
  val UserTable = TableQuery[UserTable]
}
