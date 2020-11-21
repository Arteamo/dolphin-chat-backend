package com.dolphin.db.dao

import com.dolphin.api.entity.UserJsonResponse
import com.dolphin.db.entity.RoomTable.RoomTable
import com.dolphin.db.entity.UserTable.UserTable
import com.dolphin.db.entity.UserToRoomTable.UserToRoomTable
import com.dolphin.db.entity.{Room, UserToRoom}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

trait UserToRoomDao {

  def createRelation(userId: Int, roomId: Int): Future[UserToRoom]

  def removeRelation(userId: Int, roomId: Int): Future[Int]

  def listUsersByRoomId(roomId: Int): Future[Seq[UserJsonResponse]]

  def listRoomsByUserId(userId: Int): Future[Seq[Room]]
}

class UserToRoomDaoImpl(db: Database)(implicit ec: ExecutionContext) extends UserToRoomDao {

  def createRelation(userId: Int, roomId: Int): Future[UserToRoom] = {
    val relation = UserToRoom(userId, roomId)
    val query = UserToRoomTable += relation
    db.run(query.transactionally).map(_ => relation)
  }

  override def removeRelation(userId: Int, roomId: Int): Future[Int] = {
    val query = UserToRoomTable.filter(row => row.userId === userId && row.roomId === roomId)
    db.run(query.delete.transactionally)
  }

  override def listRoomsByUserId(userId: Int): Future[Seq[Room]] = {
    val query = UserToRoomTable joinLeft RoomTable on (_.roomId === _.id) joinLeft UserTable on (_._1.userId === _.id)
    db.run(query.result).map(_.flatMap(_._1._2))
  }

  override def listUsersByRoomId(roomId: Int): Future[Seq[UserJsonResponse]] = {
    val query = UserToRoomTable joinLeft UserTable on (_.userId === _.id) joinLeft RoomTable on (_._1.roomId === _.id)
    db.run(query.result).map(_.flatMap(_._1._2.map(_.toResponse)))

  }
}
