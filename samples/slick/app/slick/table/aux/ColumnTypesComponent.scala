package slick.table.aux

import com.github.stonexx.slick.ext.HasSlickProfile
import models.Cat
import slick.MyPostgresProfile

trait ColumnTypesComponent { this: HasSlickProfile[MyPostgresProfile] =>
  import profile.api._

  implicit lazy val catStateColumnType = MappedEnumColumnType.name(Cat.State)
}
