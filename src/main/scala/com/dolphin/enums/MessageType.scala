package com.dolphin.enums

object MessageType extends Enumeration {
  type MessageType = Value
  val UserMessage, UserJoined, UserLeft = Value
}
