package com.dolphin.components.chat

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import com.dolphin.components.ComponentsHolder
import com.dolphin.components.chat.ChatMessages.{UserJoined, UserLeft, UserSaid}
import org.reactivestreams.Publisher

class ChatRoom(roomId: Int)(implicit components: ComponentsHolder) {
  implicit val system: ActorSystem = components.actorSystem
  private val roomActor = system.actorOf(Props(classOf[ChatRoomActor], roomId, components))

  def websocketFlow(userId: Int): Flow[Message, Message, Any] = {
    val (actorRef: ActorRef, publisher: Publisher[TextMessage.Strict]) =
      Source
        .actorRef[String](bufferSize = 16, OverflowStrategy.fail)
        .map(msg => TextMessage.Strict(msg))
        .toMat(Sink.asPublisher(fanout = false))(Keep.both)
        .run()
    roomActor ! UserJoined(userId, actorRef)
    val sink: Sink[Message, Any] = Flow[Message]
      .map {
        case TextMessage.Strict(msg) =>
          roomActor ! UserSaid(userId, msg)
      }
      .to(Sink.onComplete(_ => roomActor ! UserLeft(userId)))

    Flow.fromSinkAndSource(sink, Source.fromPublisher(publisher))
  }
}

object ChatRoom {
  var chatRooms: Map[Int, ChatRoom] = Map.empty[Int, ChatRoom]

  def findOrCreate(number: Int)(implicit components: ComponentsHolder): ChatRoom =
    chatRooms.getOrElse(number, createNewChatRoom(number))

  private def createNewChatRoom(number: Int)(implicit components: ComponentsHolder): ChatRoom = {
    val chatroom = new ChatRoom(number)
    chatRooms += number -> chatroom
    chatroom
  }

}
