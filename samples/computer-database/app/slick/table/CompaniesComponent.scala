package slick.table

import com.github.stonexx.slick.ext.{ExtJdbcProfile, HasSlickProfile}
import models.Company

trait CompaniesComponent { this: HasSlickProfile[ExtJdbcProfile] =>
  import profile.api._

  class Companies(tag: Tag) extends Table[Company](tag, "COMPANY") {

    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def name = column[String]("NAME")

    def * = (id.?, name) <> (Company.tupled, Company.unapply)
  }
}

