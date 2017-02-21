package slick

import com.github.stonexx.slick.ext.HasSlickProfile
import models.Test
import slick.lifted.{ProvenShape, PrimaryKey}

trait TestsComponent { this: HasSlickProfile[MyPostgresProfile] =>
  import profile.api._

  class Tests(tag: Tag) extends Table[Test](tag, "tests") {

    def a: Rep[String] = column[String]("a")
    def b: Rep[String] = column[String]("b")
    def c: Rep[String] = column[String]("c")
    def d: Rep[String] = column[String]("d")

    def * : ProvenShape[Test] = (a, b, c, d) <> ((Test.apply _).tupled, Test.unapply)

    def pk: PrimaryKey = primaryKey("tests_pkey", (a, b))
  }
}
