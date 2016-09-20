package dao

import javax.inject.{Inject, Singleton}

import com.github.stonexx.play.db.slick.Database
import com.github.stonexx.scala.data.Page
import models.{Computer, Company}
import play.api.libs.concurrent.Execution.Implicits._
import slick.ast.TypedType

import scala.concurrent.Future

@Singleton
class ComputerDao @Inject()(db: Database) {
  import slick.Tables._, profile.api._
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

    def ordered[T: TypedType](column: ((Computers, Rep[Option[Companies]])) => Rep[T])(desc: Boolean) =
      column andThen (c => (if (desc) c.desc else c.asc).nullsLast)

    def sorting(q: computersWithCompaniesQuery.type) = sortBy match {
      case S.Ordered(S.Id, desc) => q.sortBy(ordered(_._1.id)(desc))
      case S.Ordered(S.Name, desc) => q.sortBy(ordered(_._1.name)(desc))
      case S.Ordered(S.Introduced, desc) => q.sortBy(ordered(_._1.introduced)(desc))
      case S.Ordered(S.Discontinued, desc) => q.sortBy(ordered(_._1.discontinued)(desc))
      case S.Ordered(S.Company, desc) => q.sortBy(ordered(_._2.map(_.name))(desc))
    }

    db.run(for {
      total <- countAction(filter)
      list <- sorting(computersWithCompaniesQuery).drop(offset).take(size).result
    } yield Page(list, page, size, total))
  }

  def getById(id: Long): Future[Computer] = db.run(computers.filter(_.id === id).result.head)

  def findById(id: Long): Future[Option[Computer]] = db.run(computers.filter(_.id === id).result.headOption)

  def insert(computer: Computer): Future[Int] = db.run(computers += computer)

  def insert(computers: Seq[Computer]): Future[Option[Int]] = db.run(this.computers ++= computers)

  def update(id: Long, computer: Computer): Future[Int] = db.run(computers.filter(_.id === id).update(computer))

  def delete(id: Long): Future[Int] = db.run(computers.filter(_.id === id).delete)
}
