package slick

import com.github.stonexx.slick.ext.HasSlickProfile

object Tables extends Tables {
  val profile = MyPostgresProfile
}

trait Tables extends HasSlickProfile[MyPostgresProfile]
  with CommonComponent
  with CatsComponent
  with TasksComponent
  with TestsComponent
