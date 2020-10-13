package com.dolphin.components.chat

import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.scaladsl.Flow

import scala.concurrent.ExecutionContext

class ChatService()(implicit ec: ExecutionContext) {

  def echo: Flow[Message, Message, Any] = Flow[Message].map {
    case TextMessage.Strict(text) => TextMessage(s"text: $text")
    case _ => TextMessage("Message type unsupported")
  }

}
