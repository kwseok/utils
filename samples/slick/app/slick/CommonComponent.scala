package slick

import com.github.stonexx.slick.ext.HasSlickProfile
import models.Cat

trait CommonComponent { this: HasSlickProfile[MyPostgresProfile] =>
  import profile.api._

  implicit lazy val catStateColumnType: BaseColumnType[Cat.State.Value] = MappedEnumColumnType.name(Cat.State)
}
