package com.dolphin.db.dao

import com.dolphin.db.entity.Room
import com.dolphin.db.entity.RoomTable.RoomTable
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

trait RoomDao {
  def create(room: Room): Future[Room]

  def findById(id: Int): Future[Option[Room]]

  def findByTitle(title: String): Future[Seq[Room]]

  def list(): Future[Seq[Room]]

  def updateRoomImage(roomId: Int, encodedImage: String): Future[Int]
}

class RoomDaoImpl(db: Database)(implicit ec: ExecutionContext) extends RoomDao {

  override def create(room: Room): Future[Room] = {
    val query = RoomTable returning RoomTable.map(_.id) into ((room, roomId) => room.copy(id = Some(roomId))) += room
    db.run(query.transactionally)
  }

  override def findById(id: Int): Future[Option[Room]] = {
    val query = RoomTable.filter(_.id === id)
    db.run(query.result.headOption)
  }

  override def findByTitle(title: String): Future[Seq[Room]] = {
    val query = RoomTable.filter(_.title.toLowerCase like s"%${title.toLowerCase}%")
    db.run(query.result)
  }

  override def list(): Future[Seq[Room]] = {
    db.run(RoomTable.result)
  }

  override def updateRoomImage(roomId: Int, encodedImage: String): Future[Int] = {
    val query = RoomTable.filter(_.id === roomId).map(_.encodedImage).update(encodedImage)
    db.run(query.transactionally)
  }
}
