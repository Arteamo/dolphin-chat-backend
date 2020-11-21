package com.dolphin.components.chat

import akka.actor.ActorRef

object ChatMessages {

  sealed trait UserEvent

  case class UserJoined(userId: Int, userActor: ActorRef) extends UserEvent

  case class UserLeft(userId: Int) extends UserEvent

  case class UserSaid(userId: Int, message: String) extends UserEvent
}
