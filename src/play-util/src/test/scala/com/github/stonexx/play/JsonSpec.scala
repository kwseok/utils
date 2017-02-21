package com.github.stonexx.play

import com.github.stonexx.scala.data.{Limit, Page}
import org.scalatestplus.play._
import play.api.libs.json._
import play.api.test._
import play.api.test.Helpers._

class JsonSpec extends PlaySpec {

  "enum" should {
    import json.enum._

    "enumIdReads" in {
      import TestEnum._
      implicit val jsonReads = enumIdReads(TestEnum)
      JsNumber(0).as[TestEnum] mustBe A
      JsNumber(1).as[TestEnum] mustBe B
      JsNumber(2).as[TestEnum] mustBe C
    }

    "enumIdWrites" in {
      import TestEnum._
      implicit val jsonWrites = enumIdWrites[TestEnum.type]
      Json.toJson(A) mustBe JsNumber(0)
      Json.toJson(B) mustBe JsNumber(1)
      Json.toJson(C) mustBe JsNumber(2)
    }

    "enumIdFormat" in {
      import TestEnum._
      implicit val jsonFormat = enumIdFormat(TestEnum)
      JsNumber(0).as[TestEnum] mustBe A
      JsNumber(1).as[TestEnum] mustBe B
      JsNumber(2).as[TestEnum] mustBe C
      Json.toJson(A) mustBe JsNumber(0)
      Json.toJson(B) mustBe JsNumber(1)
      Json.toJson(C) mustBe JsNumber(2)
    }

    "enumNameReads" in {
      import TestEnum._
      implicit val jsonReads = enumNameReads(TestEnum)
      JsString("A").as[TestEnum] mustBe A
      JsString("B").as[TestEnum] mustBe B
      JsString("C").as[TestEnum] mustBe C
    }

    "enumNameWrites" in {
      import TestEnum._
      implicit val jsonWrites = enumNameWrites[TestEnum.type]
      Json.toJson(A) mustBe JsString("A")
      Json.toJson(B) mustBe JsString("B")
      Json.toJson(C) mustBe JsString("C")
    }

    "enumNameFormat" in {
      import TestEnum._
      implicit val jsonFormat = enumNameFormat(TestEnum)
      JsString("A").as[TestEnum] mustBe A
      JsString("B").as[TestEnum] mustBe B
      JsString("C").as[TestEnum] mustBe C
      Json.toJson(A) mustBe JsString("A")
      Json.toJson(B) mustBe JsString("B")
      Json.toJson(C) mustBe JsString("C")
    }

    "enumSortReads" in {
      import SortEnum._
      implicit val jsonReads = enumSortReads(SortEnum)
      JsString("A").as[Ordered] mustBe A.asc
      JsString("-B").as[Ordered] mustBe B.desc
      JsString("-C").as[Ordered] mustBe C.desc
    }

    "enumSortWrites" in {
      import SortEnum._
      implicit val jsonWrites = enumSortWrites[SortEnum.type]
      Json.toJson(A.asc) mustBe JsString("A")
      Json.toJson(B.desc) mustBe JsString("-B")
      Json.toJson(C.desc) mustBe JsString("-C")
    }

    "enumSortFormat" in {
      import SortEnum._
      implicit val jsonFormat = enumSortFormat(SortEnum)
      Json.toJson(A.asc).as[Ordered] mustBe A.asc
      Json.toJson(B.desc).as[Ordered] mustBe B.desc
      Json.toJson(C.desc).as[Ordered] mustBe C.desc
    }

    "enumOrderedReads" in {
      import OrderedEnum._
      implicit val jsonReads = enumOrderedReads(OrderedEnum)
      JsString("A").as[Ordered] mustBe A.asc
      JsString("-B:NF").as[Ordered] mustBe B.desc.nullsFirst
      JsString("-C:NL").as[Ordered] mustBe C.desc.nullsLast
    }

    "enumOrderedWrites" in {
      import OrderedEnum._
      implicit val jsonWrites = enumOrderedWrites[OrderedEnum.type]
      Json.toJson(A.asc) mustBe JsString("A")
      Json.toJson(B.desc.nullsFirst) mustBe JsString("-B:NF")
      Json.toJson(C.desc.nullsLast) mustBe JsString("-C:NL")
    }

    "enumOrderedFormat" in {
      import OrderedEnum._
      implicit val jsonFormat = enumOrderedFormat(OrderedEnum)
      Json.toJson(A.asc).as[Ordered] mustBe A.asc
      Json.toJson(B.desc.nullsFirst).as[Ordered] mustBe B.desc.nullsFirst
      Json.toJson(C.desc.nullsLast).as[Ordered] mustBe C.desc.nullsLast
    }
  }

  "limit" should {
    import json.limit._

    "jsonFormat" in {
      val limit = Limit(from = Some(1), to = Some(10))
      val json = Json.toJson(limit)
      limitJsonFormat[Int].reads(json).get mustBe limit
      limitJsonFormat[Int].writes(limit) mustBe json
    }
  }

  "page" should {
    import json.page._

    "jsonWrites" in {
      val page = Page[Int](1 to 10, 1, 10, 100)
      val json = Json.toJson(page)
      pageJsonWrites[Int].writes(page) mustBe json
    }
  }
}
