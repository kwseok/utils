package com.github.stonexx.play.db.anorm

import org.scalatestplus.play._
import play.api.db.Databases

object EnumerationExtensionsSpec {
  object TestEnum extends Enumeration {
    type TestEnum = Value
    val A = Value
    val B = Value
    val C = Value
  }
}

class EnumerationExtensionsSpec extends PlaySpec {
  import anorm._, SqlParser._

  "EnumerationExtensions" should {
    import EnumerationExtensionsSpec.TestEnum._

    "ToId" in {
      import enums.ToId._

      Databases.withInMemory()(_.withConnection { implicit conn =>
        SQL"select $A".as(scalar[TestEnum].single) mustBe A
        SQL"select $B".as(scalar[TestEnum].single) mustBe B
        SQL"select $C".as(scalar[TestEnum].single) mustBe C
      })
    }

    "ToName" in {
      import enums.ToName._

      Databases.withInMemory()(_.withConnection { implicit conn =>
        SQL"select $A".as(scalar[TestEnum].single) mustBe A
        SQL"select $B".as(scalar[TestEnum].single) mustBe B
        SQL"select $C".as(scalar[TestEnum].single) mustBe C
      })
    }
  }
}

