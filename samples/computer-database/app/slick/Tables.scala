package slick

import com.github.stonexx.slick.ext.{ExtH2Profile, ExtJdbcProfile, HasSlickProfile}
import table._

object Tables extends Tables {
  val profile = ExtH2Profile
}

trait Tables extends HasSlickProfile[ExtJdbcProfile]
  with aux.ColumnTypesComponent
  with CompaniesComponent
  with ComputersComponent
