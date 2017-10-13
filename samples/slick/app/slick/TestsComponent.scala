package slick

import models.Test
import play.api.db.slick.HasDatabaseConfig
import slick.lifted.{ProvenShape, PrimaryKey}

trait TestsComponent { this: HasDatabaseConfig[MyPostgresProfile] =>
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
