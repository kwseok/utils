package controllers

import javax.inject._

import dao.{ComputerDao, CompanyDao}
import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.I18nSupport
import play.api.mvc._
import play.filters.csrf._

import scala.concurrent.ExecutionContext

/** Manage a database of computers. */
@Singleton
class Application @Inject()(
  companyDao: CompanyDao,
  computerDao: ComputerDao,
  components: ControllerComponents,
  csrfAddToken: CSRFAddToken,
  csrfCheck: CSRFCheck
)(
  implicit
  assets: AssetsFinder,
  ec: ExecutionContext
) extends AbstractController(components) with I18nSupport {
  import Computer.forms._

  /** This result directly redirect to the application home. */
  val Home: Result = Redirect(routes.Application.list())

  /** Describe the computer form (used in both edit and create screens). */
  val computerForm = Form(
    mapping(
      "id" -> optional(longNumber),
      "name" -> nonEmptyText,
      "introduced" -> optional(date("yyyy-MM-dd")),
      "discontinued" -> optional(date("yyyy-MM-dd")),
      "company" -> optional(longNumber)
    )(Computer.apply)(Computer.unapply)
  )

  // -- Actions

  /** Handle default path requests, redirect to computers list */
  def index = Action(Home)

  /**
   * Display the paginated list of computers.
   *
   * @param page   Current page number (starts from 0)
   * @param filter Filter applied on computer names
   * @param sort   Column to be sorted
   */
  def list(page: Int, filter: String, sort: Option[Sorts.Ordered]): Action[AnyContent] = Action.async { implicit request =>
    val sortBy = sort.filter(_.isDefined).getOrElse(Sorts.Id.desc)
    val computers = computerDao.list(page, sortBy = sortBy, filter = "%" + filter + "%")
    computers.map(cs => Ok(views.html.list(cs, filter, sortBy)))
  }

  /**
   * Display the 'edit form' of a existing Computer.
   *
   * @param id Id of the computer to edit
   */
  def edit(id: Long): Action[AnyContent] = csrfAddToken {
    Action.async { implicit request =>
      val computerAndOptions = for {
        computer <- computerDao.findById(id)
        options <- companyDao.options
      } yield (computer, options)

      computerAndOptions.map {
        case (computer, options) => computer match {
          case Some(c) => Ok(views.html.editForm(id, computerForm.fill(c), options))
          case None => NotFound
        }
      }
    }
  }

  /**
   * Handle the 'edit form' submission
   *
   * @param id Id of the computer to edit
   */
  def update(id: Long): Action[AnyContent] = csrfCheck {
    Action.async { implicit request =>
      computerForm.bindFromRequest.fold(
        formWithErrors => companyDao.options.map { options =>
          BadRequest(views.html.editForm(id, formWithErrors, options))
        },
        computer => computerDao.update(id, computer.copy(id = Some(id))).map { _ =>
          Home.flashing("success" -> "Computer %s has been updated".format(computer.name))
        }
      )
    }
  }

  /** Display the 'new computer form'. */
  def create: Action[AnyContent] = csrfAddToken {
    Action.async { implicit request =>
      companyDao.options.map(options => Ok(views.html.createForm(computerForm, options)))
    }
  }

  /** Handle the 'new computer form' submission. */
  def save: Action[AnyContent] = csrfCheck {
    Action.async { implicit request =>
      computerForm.bindFromRequest.fold(
        formWithErrors => companyDao.options.map { options =>
          BadRequest(views.html.createForm(formWithErrors, options))
        },
        computer => computerDao.insert(computer).map { _ =>
          Home.flashing("success" -> "Computer %s has been created".format(computer.name))
        }
      )
    }
  }

  /** Handle computer deletion. */
  def delete(id: Long): Action[AnyContent] = csrfCheck {
    Action.async {
      computerDao.delete(id).map { _ =>
        Home.flashing("success" -> "Computer has been deleted")
      }
    }
  }
}
