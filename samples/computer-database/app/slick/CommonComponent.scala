package slick

import java.util.Date

import com.github.stonexx.slick.ext.{ExtJdbcProfile, HasSlickProfile}

trait CommonComponent { this: HasSlickProfile[ExtJdbcProfile] =>
  import profile.api._

  implicit lazy val javaDateColumnType: BaseColumnType[Date] = MappedColumnType.base[java.util.Date, Long](_.getTime, new Date(_))
}
