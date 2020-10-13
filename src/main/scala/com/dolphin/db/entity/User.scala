package com.dolphin.db.entity

import java.sql.Timestamp

import slick.jdbc.PostgresProfile.api._
import slick.lifted.{ProvenShape, TableQuery, Tag}

case class User(
  id: Option[Int] = None,
  login: String,
  passwordHash: String,
  email: String,
  createdTimestamp: Timestamp = new Timestamp(System.currentTimeMillis())
)

class UserTable(tag: Tag) extends Table[User](tag, "users") {

  override def * : ProvenShape[User] =
    (id.?, login, passwordHash, email, createdTimestamp) <> (User.tupled, User.unapply)

  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def login: Rep[String] = column[String]("login")

  def passwordHash: Rep[String] = column[String]("password_hash")

  def email: Rep[String] = column[String]("email")

  def createdTimestamp: Rep[Timestamp] = column[Timestamp]("created_timestamp")
}

object UserTable {
  val UserTable = TableQuery[UserTable]
}
