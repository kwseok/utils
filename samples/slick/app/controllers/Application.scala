package controllers

import javax.inject._

import actors.MyWebSocketActor
import actors.MyWebSocketActor.{InEvent, OutEvent}
import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.Source
import models._
import play.api._
import play.api.i18n.I18nSupport
import play.api.libs.Comet
import play.api.libs.json._
import play.api.libs.streams.ActorFlow
import play.api.mvc._
import play.api.routing.JavaScriptReverseRouter

import scala.concurrent.ExecutionContext

@Singleton
class Application @Inject()(
  components: ControllerComponents
)(
  implicit
  ec: ExecutionContext,
  env: Environment,
  assets: AssetsFinder,
  materializer: Materializer,
  actorSystem: ActorSystem
) extends AbstractController(components) with I18nSupport {

  private val logger = Logger(this.getClass)

  def index = Action { implicit request =>
    Ok(views.html.index("Your new application is ready."))
  }

  // JSON examples

  def listPlaces = Action {
    Ok(Json.toJson(Place.list))
  }

  def savePlace: Action[JsValue] = Action(parse.json) { request =>
    request.body.validate[Place].map { place =>
      Place.save(place)
      Ok(Json.obj(
        "status" -> "OK",
        "message" -> s"Place '${place.name}' saved."
      ))
    }.recoverTotal { errors =>
      BadRequest(Json.obj(
        "status" -> "FA",
        "errors" -> JsError.toJson(errors)
      ))
    }
    /*
    request.body.validate[Place].fold(
      errors => {
        BadRequest(Json.obj(
          "status" -> "FA",
          "errors" -> JsError.toFlatJson(errors)
        ))
      },
      place => {
        Place.save(place)
        Ok(Json.obj(
          "status" -> "OK",
          "message" -> s"Place '${place.name}' saved."
        ))
      }
    )
    */
  }

  def jsonTransform = Action {
    Ok(Json.toJson(Cat("parent", "white", flag = true, Cat.State.Good)).as[JsObject] ++ Json.obj(
      "children" -> Json.arr(
        Cat("a", "red", flag = true, Cat.State.Good),
        Cat("b", "black", flag = false, Cat.State.VeryGood)
      )
    ))
  }

  // Handling time-outs exmaple
  def timeout: Action[AnyContent] = Action.async {
    import scala.concurrent.Future
    import scala.concurrent.duration._

    val futureInt = Future {
      Thread.sleep(2000l)
      1
    }
    val timeoutFuture = akka.pattern.after(1 second, actorSystem.scheduler)(Future.successful("Oops"))

    Future.firstCompletedOf(Seq(futureInt, timeoutFuture)).map {
      case i: Int => Ok(<h1>Got result:
        {i}
      </h1>).as(HTML)
      case t: String => InternalServerError(<h1>
        {t}
      </h1>).as(HTML)
    }
  }

  // Serving files example
  def favicon = Action {
    env.resource("public/images/favicon.png").map { url =>
      logger.info("Serving file: " + url.getFile)
      val file = new java.io.File(url.getFile)
      Ok.sendFile(content = file, inline = true)
    }.getOrElse {
      NotFound(<h1>favicon.png not found</h1>).as(HTML)
    }
  }

  // Comet example
  def comet = Action { implicit request =>
    Ok(views.html.comet())
  }

  def cometEvents = Action {
    val events = Source.single(Json.arr("kiki", "foo", "bar"))
    Ok.chunked(events via Comet.json(callbackName = "parent.cometMessage")).as(HTML)
  }

  // WebSocket examples

  def webSocket = Action { implicit request =>
    Ok(views.html.webSocket())
  }

  def webSocketWS: WebSocket = WebSocket.accept[InEvent, OutEvent] { _ =>
    ActorFlow.actorRef(MyWebSocketActor.props)
  }

  // React examples

  def react(name: String) = Action { implicit request =>
    Ok(views.html.react(name))
  }

  // JavaScript Routes

  def jsRoutes = Action { implicit request =>
    Ok(
      JavaScriptReverseRouter("Routes")(
        routes.javascript.Application.listPlaces,
        routes.javascript.Application.savePlace,
        routes.javascript.Application.jsonTransform,
        routes.javascript.Application.timeout,
        routes.javascript.Application.cometEvents,
        routes.javascript.Application.webSocketWS
      )
    ) as JAVASCRIPT
  }

}
