package dao

import javax.inject.{Inject, Singleton}

import com.github.stonexx.scala.data.Page
import com.github.stonexx.slick.ext.ExtJdbcProfile
import models.{Computer, Company}
import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.{ComputersComponent, CompaniesComponent}

import scala.concurrent.{Future, ExecutionContext}

@Singleton
class ComputerDao @Inject()(
  protected val dbConfigProvider: DatabaseConfigProvider
)(implicit ec: ExecutionContext)
  extends HasDatabaseConfigProvider[ExtJdbcProfile]
    with ComputersComponent
    with CompaniesComponent {

  import profile.api._
  import Computer.forms.{Sorts => S}

  private val computers = TableQuery[Computers]
  private val companies = TableQuery[Companies]

  private def countAction(filter: String): DBIO[Int] =
    computers.filter(_.name.toLowerCase like filter.toLowerCase).length.result

  def count: Future[Int] = db.run(computers.length.result)

  def count(filter: String): Future[Int] = db.run(countAction(filter))

  def list(page: Int = 1, size: Int = 10, sortBy: S.Ordered = S.Id.desc, filter: String = "%"): Future[Page[(Computer, Option[Company])]] = {
    val offset = size * (page - 1)

    val computersWithCompaniesQuery = for {
      (computer, company) <- computers joinLeft companies on (_.companyId === _.id)
      if computer.name.toLowerCase like filter.toLowerCase
    } yield (computer, company)

    implicit val querySortCondition: QuerySortCondition[(Computers, Rep[Option[Companies]]), S.Ordered] = QueryConditions.orderCondition[(Computers, Rep[Option[Companies]]), S.type] {
      case S.Id => _._1.id
      case S.Name => _._1.name
      case S.Introduced => _._1.introduced
      case S.Discontinued => _._1.discontinued
      case S.Company => _._2.map(_.name)
    }

    db.run(for {
      total <- countAction(filter)
      list <- computersWithCompaniesQuery.sortByCondition(sortBy).drop(offset).take(size).result
    } yield Page(list, page, size, total))
  }

  def getById(id: Long): Future[Computer] = db.run(computers.filter(_.id === id).result.head)

  def findById(id: Long): Future[Option[Computer]] = db.run(computers.filter(_.id === id).result.headOption)

  def insert(computer: Computer): Future[Int] = db.run(computers += computer)

  def insert(computers: Seq[Computer]): Future[Option[Int]] = db.run(this.computers ++= computers)

  def update(id: Long, computer: Computer): Future[Int] = db.run(computers.filter(_.id === id).update(computer))

  def delete(id: Long): Future[Int] = db.run(computers.filter(_.id === id).delete)
}
