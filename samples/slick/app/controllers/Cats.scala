package controllers

import javax.inject._

import akka.stream.scaladsl.Source
import dao.CatDao
import models._
import play.api.i18n.I18nSupport
import play.api.libs.Comet
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents, AnyContent, Action}
import play.filters.csrf._

import scala.concurrent.ExecutionContext

@Singleton
class Cats @Inject()(
  catDao: CatDao,
  components: ControllerComponents,
  csrfAddToken: CSRFAddToken,
  csrfCheck: CSRFCheck
)(
  implicit
  ec: ExecutionContext,
  assets: AssetsFinder
) extends AbstractController(components) with I18nSupport {
  import Cat.forms._

  def list: Action[AnyContent] = csrfAddToken {
    Action.async { implicit request =>
      catDao.list.map { cats =>
        Ok(views.html.cats(cats, catForm))
      }
    }
  }

  def listJson: Action[AnyContent] = Action.async {
    catDao.list.map { cats =>
      Ok(Json.toJson(cats))
    }
  }

  def listComet: Action[AnyContent] = Action {
    // Records fetched in chunks of 2, and asynchronously piped out to
    // browser in chunked http responses, to be handled by comet callback.
    //
    // see http://www.playframework.com/documentation/2.5.x/ScalaComet

    val input = Source.fromPublisher(catDao.stream).grouped(2).map(Json.toJson(_))
    Ok.chunked(input via Comet.json(callbackName = "parent.cometMessage")).as(HTML)
  }

  def create: Action[AnyContent] = csrfCheck {
    Action.async { implicit request =>
      catForm.bindFromRequest.fold(
        formWithErrors => catDao.list.map { cats => Ok(views.html.cats(cats, formWithErrors)) },
        cat => catDao.insert(cat).map { _ => Redirect(routes.Cats.list()) }
      )
    }
  }

  def delete(name: String): Action[AnyContent] = csrfCheck {
    Action.async {
      catDao.delete(name).map { _ =>
        Redirect(routes.Cats.list())
      }
    }
  }
}
