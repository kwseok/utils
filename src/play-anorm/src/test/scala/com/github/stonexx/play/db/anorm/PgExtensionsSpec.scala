package com.github.stonexx.play.db.anorm

import org.scalatestplus.play._
import play.api.db.Databases
import play.api.test._
import play.api.test.Helpers._

class PgExtensionsSpec extends PlaySpec {
  import anorm._, SqlParser._

  "PgExtensions" should {
    import pg._

    "map" in {
      val map = Map("a" -> "1", "b" -> "2", "c" -> "3")
      Databases.withInMemory(urlOptions = Map("MODE" -> "PostgreSQL"))(_.withConnection { implicit conn =>
        SQL"select $map".as(scalar[Map[String, String]].single) mustBe map
      })
    }

    "ltree" in {
      val ltree = LTree(Seq("a", "b", "c", "d", "e"))
      Databases.withInMemory(urlOptions = Map("MODE" -> "PostgreSQL"))(_.withConnection { implicit conn =>
        SQL"select $ltree".as(scalar[LTree].single) mustBe ltree
      })
    }
  }
}

