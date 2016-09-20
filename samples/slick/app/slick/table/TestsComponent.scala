package slick.table

import com.github.stonexx.slick.ext.HasSlickProfile
import models.Test
import slick.MyPostgresProfile

trait TestsComponent { this: HasSlickProfile[MyPostgresProfile] =>
  import profile.api._

  class Tests(tag: Tag) extends Table[Test](tag, "tests") {

    def a = column[String]("a")
    def b = column[String]("b")
    def c = column[String]("c")
    def d = column[String]("d")

    def * = (a, b, c, d) <> ((Test.apply _).tupled, Test.unapply)

    def pk = primaryKey("tests_pkey", (a, b))
  }
}
