package com.dolphin.components.chat

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import akka.stream.{Materializer, OverflowStrategy}
import com.dolphin.components.chat.ChatMessages.{UserJoined, UserLeft, UserSaid}
import org.reactivestreams.Publisher

class ChatRoom(roomId: Int)(implicit system: ActorSystem, mat: Materializer) {
  private val roomActor = system.actorOf(Props(classOf[ChatRoomActor], roomId))

  def websocketFlow(name: String): Flow[Message, Message, Any] = {
    val (actorRef: ActorRef, publisher: Publisher[TextMessage.Strict]) =
      Source.actorRef[String](bufferSize = 16, OverflowStrategy.fail)
        .map(msg => TextMessage.Strict(msg))
        .toMat(Sink.asPublisher(fanout = false))(Keep.both).run()
    roomActor ! UserJoined(name, actorRef)
    val sink: Sink[Message, Any] = Flow[Message]
      .map {
        case TextMessage.Strict(msg) =>
          roomActor ! UserSaid(name, msg)
      }
      .to(Sink.onComplete( _ =>
        roomActor ! UserLeft(name)
      ))

    // Pair sink and source
    Flow.fromSinkAndSource(sink, Source.fromPublisher(publisher))
  }
}

object ChatRoom {
  var chatRooms: Map[Int, ChatRoom] = Map.empty[Int, ChatRoom]

  def findOrCreate(number: Int)(implicit actorSystem: ActorSystem): ChatRoom = chatRooms.getOrElse(number, createNewChatRoom(number))

  private def createNewChatRoom(number: Int)(implicit actorSystem: ActorSystem): ChatRoom = {
    val chatroom = new ChatRoom(number)
    chatRooms += number -> chatroom
    chatroom
  }

}