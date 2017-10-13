package slick

import java.util.Date

import com.github.stonexx.slick.ext.ExtJdbcProfile
import play.api.db.slick.HasDatabaseConfig

trait CommonComponent { this: HasDatabaseConfig[ExtJdbcProfile] =>
  import profile.api._

  implicit lazy val javaDateColumnType: BaseColumnType[Date] = MappedColumnType.base[java.util.Date, Long](_.getTime, new Date(_))
}
