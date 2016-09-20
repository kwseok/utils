package com.github.stonexx.play

import org.joda.time.{DateTime, LocalDate, LocalTime, Period}
import org.scalatestplus.play._
import play.api.test._
import play.api.test.Helpers._

class MvcSpec extends PlaySpec {

  "binders" should {

    "enum" should {
      import mvc.binders.enum._

      "enumIdPathBindable" in {
        import TestEnum._
        val pathBindable = enumIdPathBindable(TestEnum)
        pathBindable.bind("a", "0").right.get mustBe A
        pathBindable.bind("b", "1").right.get mustBe B
        pathBindable.bind("c", "2").right.get mustBe C
        pathBindable.unbind("a", A) mustBe "0"
        pathBindable.unbind("b", B) mustBe "1"
        pathBindable.unbind("c", C) mustBe "2"
      }

      "enumIdQueryStringBindable" in {
        import TestEnum._
        val queryStringBindable = enumIdQueryStringBindable(TestEnum)
        queryStringBindable.bind("a", Map("a" -> Seq("0"))).get.right.get mustBe A
        queryStringBindable.bind("b", Map("b" -> Seq("1"))).get.right.get mustBe B
        queryStringBindable.bind("c", Map("c" -> Seq("2"))).get.right.get mustBe C
        queryStringBindable.unbind("a", A) mustBe "a=0"
        queryStringBindable.unbind("b", B) mustBe "b=1"
        queryStringBindable.unbind("c", C) mustBe "c=2"
      }

      "enumNamePathBindable" in {
        import TestEnum._
        val pathBindable = enumNamePathBindable(TestEnum)
        pathBindable.bind("a", "A").right.get mustBe A
        pathBindable.bind("b", "B").right.get mustBe B
        pathBindable.bind("c", "C").right.get mustBe C
        pathBindable.unbind("a", A) mustBe "A"
        pathBindable.unbind("b", B) mustBe "B"
        pathBindable.unbind("c", C) mustBe "C"
      }

      "enumNameQueryStringBindable" in {
        import TestEnum._
        val queryStringBindable = enumNameQueryStringBindable(TestEnum)
        queryStringBindable.bind("a", Map("a" -> Seq("A"))).get.right.get mustBe A
        queryStringBindable.bind("b", Map("b" -> Seq("B"))).get.right.get mustBe B
        queryStringBindable.bind("c", Map("c" -> Seq("C"))).get.right.get mustBe C
        queryStringBindable.unbind("a", A) mustBe "a=A"
        queryStringBindable.unbind("b", B) mustBe "b=B"
        queryStringBindable.unbind("c", C) mustBe "c=C"
      }

      "enumSortPathBindable" in {
        import SortEnum._
        val pathBindable = enumSortPathBindable(SortEnum)
        pathBindable.bind("", "A").right.get mustBe A.asc
        pathBindable.bind("", "B").right.get mustBe B.asc
        pathBindable.bind("", "C").right.get mustBe C.asc
        pathBindable.bind("", "+A").right.get mustBe A.asc
        pathBindable.bind("", "+B").right.get mustBe B.asc
        pathBindable.bind("", "+C").right.get mustBe C.asc
        pathBindable.bind("", "-A").right.get mustBe A.desc
        pathBindable.bind("", "-B").right.get mustBe B.desc
        pathBindable.bind("", "-C").right.get mustBe C.desc
        pathBindable.unbind("", A.asc) mustBe "A"
        pathBindable.unbind("", B.asc) mustBe "B"
        pathBindable.unbind("", C.asc) mustBe "C"
        pathBindable.unbind("", A.desc) mustBe "-A"
        pathBindable.unbind("", B.desc) mustBe "-B"
        pathBindable.unbind("", C.desc) mustBe "-C"
      }

      "enumSortQueryStringBindable" in {
        import SortEnum._
        val params1 = Map("a" -> Seq("A"), "b" -> Seq("B"), "c" -> Seq("C"))
        val params2 = Map("a" -> Seq("+A"), "b" -> Seq("+B"), "c" -> Seq("+C"))
        val params3 = Map("a" -> Seq("-A"), "b" -> Seq("-B"), "c" -> Seq("-C"))
        val queryStringBindable = enumSortQueryStringBindable(SortEnum)
        queryStringBindable.bind("a", params1).get.right.get mustBe A.asc
        queryStringBindable.bind("b", params1).get.right.get mustBe B.asc
        queryStringBindable.bind("c", params1).get.right.get mustBe C.asc
        queryStringBindable.bind("a", params2).get.right.get mustBe A.asc
        queryStringBindable.bind("b", params2).get.right.get mustBe B.asc
        queryStringBindable.bind("c", params2).get.right.get mustBe C.asc
        queryStringBindable.bind("a", params3).get.right.get mustBe A.desc
        queryStringBindable.bind("b", params3).get.right.get mustBe B.desc
        queryStringBindable.bind("c", params3).get.right.get mustBe C.desc
        queryStringBindable.unbind("a", A.asc) mustBe "a=" + A.asc.toString
        queryStringBindable.unbind("b", B.asc) mustBe "b=" + B.asc.toString
        queryStringBindable.unbind("c", C.asc) mustBe "c=" + C.asc.toString
        queryStringBindable.unbind("a", A.desc) mustBe "a=" + A.desc.toString
        queryStringBindable.unbind("b", B.desc) mustBe "b=" + B.desc.toString
        queryStringBindable.unbind("c", C.desc) mustBe "c=" + C.desc.toString
      }

      "enumOrderedPathBindable" in {
        import OrderedEnum._
        val pathBindable = enumOrderedPathBindable(OrderedEnum)
        pathBindable.bind("", "A").right.get mustBe A.asc
        pathBindable.bind("", "B").right.get mustBe B.asc
        pathBindable.bind("", "C").right.get mustBe C.asc
        pathBindable.bind("", "+A").right.get mustBe A.asc
        pathBindable.bind("", "+B").right.get mustBe B.asc
        pathBindable.bind("", "+C:NF").right.get mustBe C.asc.nullsFirst
        pathBindable.bind("", "-A").right.get mustBe A.desc
        pathBindable.bind("", "-B").right.get mustBe B.desc
        pathBindable.bind("", "-C:NL").right.get mustBe C.desc.nullsLast
        pathBindable.unbind("", A.asc) mustBe A.asc.toString
        pathBindable.unbind("", B.asc) mustBe B.asc.toString
        pathBindable.unbind("", C.asc) mustBe C.asc.toString
        pathBindable.unbind("", A.desc) mustBe A.desc.toString
        pathBindable.unbind("", B.desc) mustBe B.desc.toString
        pathBindable.unbind("", C.desc) mustBe C.desc.toString
      }

      "enumOrderedQueryStringBindable" in {
        import OrderedEnum._
        val params1 = Map("a" -> Seq("A"), "b" -> Seq("B"), "c" -> Seq("C:NF"))
        val params2 = Map("a" -> Seq("+A"), "b" -> Seq("+B"), "c" -> Seq("+C"))
        val params3 = Map("a" -> Seq("-A"), "b" -> Seq("-B"), "c" -> Seq("-C:NL"))
        val queryStringBindable = enumOrderedQueryStringBindable(OrderedEnum)
        queryStringBindable.bind("a", params1).get.right.get mustBe A.asc
        queryStringBindable.bind("b", params1).get.right.get mustBe B.asc
        queryStringBindable.bind("c", params1).get.right.get mustBe C.asc.nullsFirst
        queryStringBindable.bind("a", params2).get.right.get mustBe A.asc
        queryStringBindable.bind("b", params2).get.right.get mustBe B.asc
        queryStringBindable.bind("c", params2).get.right.get mustBe C.asc
        queryStringBindable.bind("a", params3).get.right.get mustBe A.desc
        queryStringBindable.bind("b", params3).get.right.get mustBe B.desc
        queryStringBindable.bind("c", params3).get.right.get mustBe C.desc.nullsLast
        queryStringBindable.unbind("a", A.asc) mustBe "a=A"
        queryStringBindable.unbind("b", B.asc) mustBe "b=B"
        queryStringBindable.unbind("c", C.asc.nullsFirst) mustBe "c=C:NF"
        queryStringBindable.unbind("a", A.desc) mustBe "a=-A"
        queryStringBindable.unbind("b", B.desc) mustBe "b=-B"
        queryStringBindable.unbind("c", C.desc.nullsLast) mustBe "c=-C:NL"
      }
    }

    "joda" should {
      import util.JodaFormatters._
      import mvc.binders.joda._

      "pathBindableJodaDateTime" in {
        val now = DateTime.now
        val nowString = now.toString(isoDateTimeFormatter)
        val bindable = pathBindableJodaDateTime(isoDateTimeFormatter)
        bindable.bind("now", nowString).right.get mustBe now
        bindable.unbind("now", now) mustBe nowString
      }

      "pathBindableJodaLocalDate" in {
        val today = LocalDate.now
        val todayString = today.toString(isoDateFormatter)
        val bindable = pathBindableJodaLocalDate(isoDateFormatter)
        bindable.bind("today", todayString).right.get mustBe today
        bindable.unbind("today", today) mustBe todayString
      }

      "pathBindableJodaLocalTime" in {
        val now = LocalTime.now
        val nowString = now.toString(isoTimeFormatter)
        val bindable = pathBindableJodaLocalTime(isoTimeFormatter)
        bindable.bind("now", nowString).right.get mustBe now
        bindable.unbind("now", now) mustBe nowString
      }

      "pathBindableJodaPeriod" in {
        val week = Period.weeks(1)
        val weekString = week.toString(isoPeriodFormatter)
        val bindable = pathBindableJodaPeriod(isoPeriodFormatter)
        bindable.bind("week", weekString).right.get mustBe week
        bindable.unbind("week", week) mustBe weekString
      }

      "queryStringBindableJodaDateTime" in {
        val now = DateTime.now
        val nowString = now.toString(isoDateTimeFormatter)
        val bindable = queryStringBindableJodaDateTime(isoDateTimeFormatter)
        bindable.bind("now", Map("now" -> Seq(nowString))).get.right.get mustBe now
        bindable.unbind("now", now) mustBe "now=" + nowString
      }

      "queryStringBindableJodaLocalDate" in {
        val today = LocalDate.now
        val todayString = today.toString(isoDateFormatter)
        val bindable = queryStringBindableJodaLocalDate(isoDateFormatter)
        bindable.bind("today", Map("today" -> Seq(todayString))).get.right.get mustBe today
        bindable.unbind("today", today) mustBe "today=" + todayString
      }

      "queryStringBindableJodaLocalTime" in {
        val now = LocalTime.now
        val nowString = now.toString(isoTimeFormatter)
        val bindable = queryStringBindableJodaLocalTime(isoTimeFormatter)
        bindable.bind("now", Map("now" -> Seq(nowString))).get.right.get mustBe now
        bindable.unbind("now", now) mustBe "now=" + nowString
      }

      "queryStringBindableJodaPeriod" in {
        val week = Period.weeks(1)
        val weekString = week.toString(isoPeriodFormatter)
        val bindable = queryStringBindableJodaPeriod(isoPeriodFormatter)
        bindable.bind("week", Map("week" -> Seq(weekString))).get.right.get mustBe week
        bindable.unbind("week", week) mustBe "week=" + weekString
      }
    }

    "option" should {
      import mvc.binders.option._

      "pathBindableOption" in {
        val pathBindable = pathBindableOption[Int]
        pathBindable.bind("", "0").right.get mustBe Some(0)
        pathBindable.bind("", "1").right.get mustBe Some(1)
        pathBindable.bind("", "2").right.get mustBe Some(2)
        pathBindable.unbind("", Some(0)) mustBe "0"
        pathBindable.unbind("", Some(1)) mustBe "1"
        pathBindable.unbind("", Some(2)) mustBe "2"
      }
    }
  }
}
