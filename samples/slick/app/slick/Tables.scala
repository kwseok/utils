package slick

import com.github.stonexx.slick.ext.HasSlickProfile
import slick.table._

object Tables extends Tables {
  val profile = MyPostgresProfile
}

trait Tables extends HasSlickProfile[MyPostgresProfile]
  with aux.ColumnTypesComponent
  with CatsComponent
  with TasksComponent
  with TestsComponent
