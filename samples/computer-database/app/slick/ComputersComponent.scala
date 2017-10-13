package slick

import java.util.Date

import com.github.stonexx.slick.ext.ExtJdbcProfile
import models.Computer
import play.api.db.slick.HasDatabaseConfig
import slick.lifted.ProvenShape

trait ComputersComponent extends CommonComponent { this: HasDatabaseConfig[ExtJdbcProfile] =>
  import profile.api._

  class Computers(tag: Tag) extends Table[Computer](tag, "COMPUTER") {

    def id: Rep[Long] = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def name: Rep[String] = column[String]("NAME")
    def introduced: Rep[Option[Date]] = column[Option[Date]]("INTRODUCED")
    def discontinued: Rep[Option[Date]] = column[Option[Date]]("DISCONTINUED")
    def companyId: Rep[Option[Long]] = column[Option[Long]]("COMPANY_ID")

    def * : ProvenShape[Computer] = (
      id.?, name, introduced, discontinued, companyId
    ) <> ((Computer.apply _).tupled, Computer.unapply)
  }
}
