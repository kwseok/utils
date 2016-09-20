package slick.table

import java.util.Date

import com.github.stonexx.slick.ext.{ExtJdbcProfile, HasSlickProfile}
import models.Computer

trait ComputersComponent { this: HasSlickProfile[ExtJdbcProfile] with aux.ColumnTypesComponent =>
  import profile.api._

  class Computers(tag: Tag) extends Table[Computer](tag, "COMPUTER") {

    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def name = column[String]("NAME")
    def introduced = column[Option[Date]]("INTRODUCED")
    def discontinued = column[Option[Date]]("DISCONTINUED")
    def companyId = column[Option[Long]]("COMPANY_ID")

    def * = (id.?, name, introduced, discontinued, companyId) <> ((Computer.apply _).tupled, Computer.unapply)
  }
}
