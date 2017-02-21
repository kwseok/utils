package slick

import com.github.stonexx.slick.ext.{ExtH2Profile, ExtJdbcProfile, HasSlickProfile}

object Tables extends Tables {
  val profile = ExtH2Profile
}

trait Tables extends HasSlickProfile[ExtJdbcProfile]
  with CommonComponent
  with CompaniesComponent
  with ComputersComponent
