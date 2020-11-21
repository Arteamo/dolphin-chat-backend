package com.dolphin.components.chat

import akka.actor.{Actor, ActorRef}
import com.dolphin.api.entity.MessageJson
import com.dolphin.api.entity.MessageJson._
import com.dolphin.components.ComponentsHolder
import com.dolphin.components.chat.ChatMessages.{UserJoined, UserLeft, UserSaid}
import com.dolphin.utils.Logging
import io.circe.parser._

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global

class ChatRoomActor(roomId: Int)(components: ComponentsHolder) extends Actor with Logging {

  val users: mutable.Map[Int, ActorRef] = mutable.Map.empty[Int, ActorRef]

  override def receive: Receive = {
    case UserJoined(userId, actorRef) =>
      users.put(userId, actorRef)

    case UserLeft(userId) =>
      users.remove(userId)

    case UserSaid(userId, msg) =>
      parse(msg).flatMap(_.as[MessageJson]) match {
        case Left(e) =>
          log.error(s"Error during parsing message in $roomId for $userId", e)
        case Right(v) =>
          components.messageDao.create(v.toMessage(senderId = userId, roomId = roomId))
            .map(_ => broadcast(msg))
            .recover {
            case e: Exception => log.error("Error during message transmission", e)
          }
      }
  }

  def broadcast(msg: String): Unit = {
    users.values.foreach(_ ! msg)
  }
}
