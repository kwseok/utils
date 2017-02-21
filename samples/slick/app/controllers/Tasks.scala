package controllers

import javax.inject._

import dao.TaskDao
import models._
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.mvc._
import play.api.routing.JavaScriptReverseRouter

import scala.concurrent.ExecutionContext

@Singleton
class Tasks @Inject()(
  taskDao: TaskDao,
  components: ControllerComponents
)(implicit ec: ExecutionContext) extends AbstractController(components) with I18nSupport {
  import Task.forms._

  def index: Action[AnyContent] = Action.async { implicit request =>
    taskDao.list.map { tasks =>
      render {
        case Accepts.Html() => Ok(views.html.tasks(tasks, labelForm))
        case Accepts.Json() => Ok(Json.toJson(tasks))
      }
    }
  }

  def create: Action[AnyContent] = Action.async { implicit request =>
    labelForm.bindFromRequest.fold(
      formWithErrors => taskDao.list.map { tasks => BadRequest(views.html.tasks(tasks, formWithErrors)) },
      label => taskDao.insert(label).map { _ => Redirect(routes.Tasks.index()) }
    )
  }

  def delete(id: Long): Action[AnyContent] = Action.async {
    taskDao.delete(id).map { _ =>
      Redirect(routes.Tasks.index())
    }
  }

  // JavaScript Routes

  def jsRoutes = Action { implicit request =>
    Ok(
      JavaScriptReverseRouter("Routes")(
        routes.javascript.Tasks.index,
        routes.javascript.Tasks.create,
        routes.javascript.Tasks.delete
      )
    ) as JAVASCRIPT
  }
}
