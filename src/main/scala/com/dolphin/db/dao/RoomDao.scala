package com.dolphin.db.dao

import com.dolphin.db.entity.Room
import com.dolphin.db.entity.RoomTable.RoomTable
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

trait RoomDao {
  def create(room: Room): Future[Room]

  def findById(id: Int): Future[Option[Room]]

  def list(): Future[Seq[Room]]
}

class RoomDaoImpl(db: Database)(implicit ec: ExecutionContext) extends RoomDao {

  override def create(room: Room): Future[Room] = {
    val query = RoomTable += room
    db.run(query.transactionally).map(_ => room)
  }

  override def findById(id: Int): Future[Option[Room]] = {
    val query = RoomTable.filter(_.id === id)
    db.run(query.result.headOption)
  }

  override def list(): Future[Seq[Room]] = {
    db.run(RoomTable.result)
  }
}
