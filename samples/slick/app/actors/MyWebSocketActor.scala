package actors

import akka.actor.{ActorRef, Actor, Props}
import play.api.libs.json._
import play.api.mvc.WebSocket.MessageFlowTransformer

object MyWebSocketActor {

  case class InEvent(message: String)
  object InEvent {
    implicit val inEventFormat: OFormat[InEvent] = Json.format[InEvent]
  }

  case class OutEvent(message: String)
  object OutEvent {
    implicit val outEventFormat: OFormat[OutEvent] = Json.format[OutEvent]
  }

  implicit val messageFlowTransformer: MessageFlowTransformer[InEvent, OutEvent] =
    MessageFlowTransformer.jsonMessageFlowTransformer[InEvent, OutEvent]

  def props(out: ActorRef) = Props(new MyWebSocketActor(out))
}

class MyWebSocketActor(out: ActorRef) extends Actor {
  import MyWebSocketActor._

  def receive: Receive = {
    case msg: InEvent => out ! OutEvent("I received your message : " + msg)
  }
}
