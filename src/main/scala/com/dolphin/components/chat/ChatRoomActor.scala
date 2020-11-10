package com.dolphin.components.chat

import akka.actor.{Actor, ActorRef}
import com.dolphin.components.chat.ChatMessages.{UserJoined, UserLeft, UserSaid}

import scala.collection.mutable

class ChatRoomActor(roomId: Int) extends Actor {

  val users: mutable.Map[String, ActorRef] = mutable.Map.empty[String, ActorRef]

  override def receive: Receive = {
    case UserJoined(name, actorRef) =>
      users.put(name, actorRef)

    case UserLeft(name) =>
      users.remove(name)

    case UserSaid(name, msg) =>
      broadcast(msg)
  }

  def broadcast(msg: String): Unit = {
    users.values.foreach(_ ! msg)
  }
}
