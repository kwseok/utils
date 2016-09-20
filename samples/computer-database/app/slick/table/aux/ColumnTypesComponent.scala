package slick.table.aux

import java.util.Date

import com.github.stonexx.slick.ext.{ExtJdbcProfile, HasSlickProfile}

trait ColumnTypesComponent { this: HasSlickProfile[ExtJdbcProfile] =>
  import profile.api._

  implicit lazy val javaDateColumnType = MappedColumnType.base[java.util.Date, Long](d => d.getTime, d => new Date(d))
}
