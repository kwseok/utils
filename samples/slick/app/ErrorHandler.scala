import javax.inject._

import play.api._
import play.api.http.DefaultHttpErrorHandler
import play.api.mvc.Results._
import play.api.mvc._
import play.api.routing.Router

import scala.concurrent.Future

@Singleton
class ErrorHandler @Inject()(
  env: Environment,
  config: Configuration,
  sourceMapper: OptionalSourceMapper,
  router: Provider[Router]
) extends DefaultHttpErrorHandler(env, config, sourceMapper, router) {

  override def onProdServerError(request: RequestHeader, exception: UsefulException): Future[Result] = Future.successful {
    InternalServerError("A server error occurred: " + exception.getMessage)
  }

  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = env.mode match {
    case Mode.Prod => Future.successful(Results.Status(statusCode)(message))
    case _ => super.onClientError(request, statusCode, message)
  }
}