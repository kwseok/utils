package filters

import javax.inject.{Inject, Singleton}

import akka.stream.Materializer
import play.api.Logger
import play.api.mvc._

import scala.concurrent.{Future, ExecutionContext}

@Singleton
class LoggingFilter @Inject()(implicit override val mat: Materializer, ec: ExecutionContext) extends Filter {
  def apply(next: RequestHeader => Future[Result])(request: RequestHeader): Future[Result] = {
    val startTime = System.currentTimeMillis
    next(request).map { result =>
      val endTime = System.currentTimeMillis
      val requestTime = endTime - startTime
      Logger.info(s"${request.method} ${request.uri} took ${requestTime}ms and returned ${result.header.status}")
      result.withHeaders("Request-Time" -> requestTime.toString)
    }
  }
}
