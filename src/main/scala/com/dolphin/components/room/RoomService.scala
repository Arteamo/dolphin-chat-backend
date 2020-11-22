package com.dolphin.components.room

import com.dolphin.api.entity.{LastMessageJson, RoomJson}
import com.dolphin.db.dao.{MessageDao, UserToRoomDao}
import com.dolphin.db.entity.{Message, Room, User}
import scala.concurrent.{ExecutionContext, Future}

class RoomService(userToRoomDao: UserToRoomDao, messageDao: MessageDao)(
  implicit ec: ExecutionContext
) {

  def listRoomsByUserWithLastMessage(userId: Int): Future[Seq[RoomJson]] = {
    userToRoomDao.listRoomsByUserId(userId).flatMap { rooms =>
      Future.sequence(rooms.map { room =>
        room.id
          .map(id => messageDao.lastMessage(id))
          .getOrElse(Future.successful(None))
          .map(transform(_, room))
      })
    }
  }

  private def getRoomJson(room: Room, message: Option[Message], author: Option[User]): RoomJson = {
    RoomJson(
      room,
      message.map { msg =>
        LastMessageJson(
          msg.toMessageJson,
          author.map(_.toResponse)
        )
      }
    )
  }

  private def transform(data: Option[(Message, Option[User])], room: Room): RoomJson = {
    getRoomJson(
      room,
      data.map(_._1),
      data.flatMap(_._2)
    )
  }
}
