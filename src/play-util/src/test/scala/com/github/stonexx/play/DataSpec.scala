package com.github.stonexx.play

import com.github.stonexx.scala.data.Limit
import org.scalatestplus.play._
import play.api.data.Forms._
import play.api.test._
import play.api.test.Helpers._

class DataSpec extends PlaySpec {

  "formats" should {

    "enum" should {
      import data.formats.enum._

      "enumIdFormat" in {
        import TestEnum._
        val formatter = enumIdFormat(TestEnum)
        formatter.bind("", Map("" -> "0")).right.get mustBe A
        formatter.bind("", Map("" -> "1")).right.get mustBe B
        formatter.bind("", Map("" -> "2")).right.get mustBe C
        formatter.unbind("", A) mustBe Map("" -> "0")
        formatter.unbind("", B) mustBe Map("" -> "1")
        formatter.unbind("", C) mustBe Map("" -> "2")
      }

      "enumNameFormat" in {
        import TestEnum._
        val formatter = enumNameFormat(TestEnum)
        formatter.bind("", Map("" -> "A")).right.get mustBe A
        formatter.bind("", Map("" -> "B")).right.get mustBe B
        formatter.bind("", Map("" -> "C")).right.get mustBe C
        formatter.unbind("", A) mustBe Map("" -> "A")
        formatter.unbind("", B) mustBe Map("" -> "B")
        formatter.unbind("", C) mustBe Map("" -> "C")
      }

      "enumSortFormat" in {
        import SortEnum._
        val formatter = enumSortFormat(SortEnum)
        formatter.bind("", Map("" -> "A")).right.get mustBe A.asc
        formatter.bind("", Map("" -> "-B")).right.get mustBe B.desc
        formatter.bind("", Map("" -> "-C")).right.get mustBe C.desc
        formatter.unbind("", A.asc) mustBe Map("" -> "A")
        formatter.unbind("", B.desc) mustBe Map("" -> "-B")
        formatter.unbind("", C.desc) mustBe Map("" -> "-C")
      }

      "enumOrderedFormat" in {
        import OrderedEnum._
        val formatter = enumOrderedFormat(OrderedEnum)
        formatter.bind("", Map("" -> "A")).right.get mustBe A.asc
        formatter.bind("", Map("" -> "-B:NF")).right.get mustBe B.desc.nullsFirst
        formatter.bind("", Map("" -> "-C:NL")).right.get mustBe C.desc.nullsLast
        formatter.unbind("", A.asc) mustBe Map("" -> "A")
        formatter.unbind("", B.desc.nullsFirst) mustBe Map("" -> "-B:NF")
        formatter.unbind("", C.desc.nullsLast) mustBe Map("" -> "-C:NL")
      }
    }
  }

  "forms" should {

    "enum" should {
      import data.forms.enum._

      "enumId" in {
        import TestEnum._
        val mapping = enumId(TestEnum)
        mapping.bind(Map("" -> "0")).right.get mustBe A
        mapping.bind(Map("" -> "1")).right.get mustBe B
        mapping.bind(Map("" -> "2")).right.get mustBe C
        mapping.unbind(A) mustBe Map("" -> "0")
        mapping.unbind(B) mustBe Map("" -> "1")
        mapping.unbind(C) mustBe Map("" -> "2")
      }

      "enumName" in {
        import TestEnum._
        val mapping = enumName(TestEnum)
        mapping.bind(Map("" -> "A")).right.get mustBe A
        mapping.bind(Map("" -> "B")).right.get mustBe B
        mapping.bind(Map("" -> "C")).right.get mustBe C
        mapping.unbind(A) mustBe Map("" -> "A")
        mapping.unbind(B) mustBe Map("" -> "B")
        mapping.unbind(C) mustBe Map("" -> "C")
      }

      "enumSort" in {
        import SortEnum._
        val mapping = enumSort(SortEnum)
        mapping.bind(Map("" -> "A")).right.get mustBe A.asc
        mapping.bind(Map("" -> "-B")).right.get mustBe B.desc
        mapping.bind(Map("" -> "-C")).right.get mustBe C.desc
        mapping.unbind(A.asc) mustBe Map("" -> "A")
        mapping.unbind(B.desc) mustBe Map("" -> "-B")
        mapping.unbind(C.desc) mustBe Map("" -> "-C")
      }

      "enumOrdered" in {
        import OrderedEnum._
        val mapping = enumOrdered(OrderedEnum)
        mapping.bind(Map("" -> "A")).right.get mustBe A.asc
        mapping.bind(Map("" -> "-B:NF")).right.get mustBe B.desc.nullsFirst
        mapping.bind(Map("" -> "-C:NL")).right.get mustBe C.desc.nullsLast
        mapping.unbind(A.asc) mustBe Map("" -> "A")
        mapping.unbind(B.desc.nullsFirst) mustBe Map("" -> "-B:NF")
        mapping.unbind(C.desc.nullsLast) mustBe Map("" -> "-C:NL")
      }
    }

    "limit" in {
      import data.forms.limit._

      limit(number).bind(Map("from" -> "1")).right.get mustBe Limit(from = Some(1), to = None)
      limit(number).unbind(Limit(from = Some(1), to = None)) mustBe Map("from" -> "1")
      limit(number).bind(Map("to" -> "10")).right.get mustBe Limit(from = None, to = Some(10))
      limit(number).unbind(Limit(from = None, to = Some(10))) mustBe Map("to" -> "10")
      limit(number).bind(Map("from" -> "1", "to" -> "10")).right.get mustBe Limit(from = Some(1), to = Some(10))
      limit(number).unbind(Limit(from = Some(1), to = Some(10))) mustBe Map("from" -> "1", "to" -> "10")
      limit(number).bind(Map("eq" -> "1")).right.get mustBe Limit(from = Some(1), to = Some(1))
      limit(number).unbind(Limit(from = Some(1), to = Some(1))) mustBe Map("from" -> "1", "to" -> "1", "eq" -> "1")
    }
  }
}
