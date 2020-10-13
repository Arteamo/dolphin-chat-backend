package com.dolphin.db.entity

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import slick.jdbc.PostgresProfile.api._
import slick.lifted.{ProvenShape, TableQuery, Tag}

case class Token(
  token: String,
  userId: Int
)

object Token {
  implicit val tokenEncoder: Encoder[Token] = deriveEncoder[Token]

  implicit val tokenDecoder: Decoder[Token] = deriveDecoder[Token]
}

class TokenTable(tag: Tag) extends Table[Token](tag, "tokens") {
  override def * : ProvenShape[Token] = (token, userId) <> ((Token.apply _).tupled, Token.unapply)

  def token: Rep[String] = column[String]("token", O.PrimaryKey)

  def userId: Rep[Int] = column[Int]("user_id", O.Unique)
}

object TokenTable {
  val TokenTable = TableQuery[TokenTable]
}
