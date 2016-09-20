package dao

import javax.inject.{Inject, Singleton}

import com.github.stonexx.play.db.slick.Database
import models.Company
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.Future

@Singleton
class CompanyDao @Inject()(db: Database) {
  import slick.Tables._, profile.api._

  private val companies = TableQuery[Companies]

  /** Construct the Seq[(String,String)] needed to fill a select options set */
  def options: Future[Seq[(String, String)]] = {
    val q = companies.map(c => c.id -> c.name).sortBy { case (_, name) => name }
    db.run(q.result.map(rows => rows.map { case (id, name) => (id.toString, name) }))
  }

  def insert(companies: Seq[Company]): Future[Option[Int]] = db.run(this.companies ++= companies)
}
