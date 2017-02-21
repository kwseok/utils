package com.github.stonexx.scala.util

import java.util.{TimeZone, Locale, Calendar, Date}

import org.scalatest._

import scala.concurrent.duration._

class DateOpsSpec extends FlatSpec with Matchers {
  import date._

  def getDate(
    year: Int, month: Int, date: Int,
    hourOfDay: Int, minute: Int, second: Int, millisecond: Int,
    timeZone: TimeZone = TimeZone.getDefault,
    locale: Locale = Locale.getDefault
  ): Date = {
    val calendar = Calendar.getInstance(timeZone, locale)
    calendar.set(year, month - 1, date, hourOfDay, minute, second)
    calendar.set(Calendar.MILLISECOND, millisecond)
    calendar.getTime
  }

  "#format" should "날짜를 주어진 패턴의 문자열로 변환" in {
    getDate(2014, 7, 27, 13, 11, 50, 0).format("yyyy-MM-dd HH:mm:ss") shouldBe "2014-07-27 13:11:50"
    getDate(2014, 7, 27, 22, 0, 0, 0, timeZone = TimeZone.getTimeZone("GMT")).format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("Asia/Seoul")) shouldBe "2014-07-28 07:00:00"
  }

  "#addYears" should "날짜의 년도를 더한다" in {
    getDate(2014, 1, 1, 0, 0, 0, 0).addYears(1).format("yyyy-MM-dd a HH:mm:ss", locale = Locale.KOREA) shouldBe "2015-01-01 오전 00:00:00"
  }

  "#addMonths" should "날짜의 월을 더한다" in {
    getDate(2014, 1, 1, 0, 0, 0, 0).addMonths(1).format("yyyy-MM-dd a HH:mm:ss", locale = Locale.KOREA) shouldBe "2014-02-01 오전 00:00:00"
  }

  "#addDays" should "날짜의 일자를 더한다" in {
    getDate(2014, 1, 1, 0, 0, 0, 0).addDays(1).format("yyyy-MM-dd a HH:mm:ss", locale = Locale.KOREA) shouldBe "2014-01-02 오전 00:00:00"
  }

  "#addWeeks" should "날짜의 주를 더한다" in {
    getDate(2014, 1, 1, 0, 0, 0, 0).addWeeks(1).format("yyyy-MM-dd a HH:mm:ss", locale = Locale.KOREA) shouldBe "2014-01-08 오전 00:00:00"
  }

  "#addHours" should "날짜의 시간을 더한다" in {
    getDate(2014, 1, 1, 0, 0, 0, 0).addHours(1).format("yyyy-MM-dd a HH:mm:ss", locale = Locale.KOREA) shouldBe "2014-01-01 오전 01:00:00"
  }

  "#addMinutes" should "날짜의 분을 더한다" in {
    getDate(2014, 1, 1, 0, 0, 0, 0).addMinutes(1).format("yyyy-MM-dd a HH:mm:ss", locale = Locale.KOREA) shouldBe "2014-01-01 오전 00:01:00"
  }

  "#addSeconds" should "날짜의 초를 더한다" in {
    getDate(2014, 1, 1, 0, 0, 0, 0).addSeconds(1).format("yyyy-MM-dd a HH:mm:ss", locale = Locale.KOREA) shouldBe "2014-01-01 오전 00:00:01"
  }

  "#addMilliseconds" should "날짜의 밀리초를 더한다" in {
    getDate(2014, 1, 1, 0, 0, 0, 0).addMilliseconds(1).format("yyyy-MM-dd a HH:mm:ss SSS", locale = Locale.KOREA) shouldBe "2014-01-01 오전 00:00:00 001"
  }

  "#withYears" should "날짜의 년도를 설정한다" in {
    getDate(2014, 1, 1, 0, 0, 0, 0).withYears(2015).format("yyyy-MM-dd a HH:mm:ss", locale = Locale.KOREA) shouldBe "2015-01-01 오전 00:00:00"
  }

  "#withMonths" should "날짜의 월을 설정한다" in {
    getDate(2014, 1, 1, 0, 0, 0, 0).withMonths(2).format("yyyy-MM-dd a HH:mm:ss", locale = Locale.KOREA) shouldBe "2014-02-01 오전 00:00:00"
  }

  "#withDays" should "날짜의 일자를 설정한다" in {
    getDate(2014, 1, 1, 0, 0, 0, 0).withDays(2).format("yyyy-MM-dd a HH:mm:ss", locale = Locale.KOREA) shouldBe "2014-01-02 오전 00:00:00"
  }

  "#withHours" should "날짜의 시간을 설정한다" in {
    getDate(2014, 1, 1, 0, 0, 0, 0).withHours(1).format("yyyy-MM-dd a HH:mm:ss", locale = Locale.KOREA) shouldBe "2014-01-01 오전 01:00:00"
  }

  "#withMinutes" should "날짜의 분을 설정한다" in {
    getDate(2014, 1, 1, 0, 0, 0, 0).withMinutes(1).format("yyyy-MM-dd a HH:mm:ss", locale = Locale.KOREA) shouldBe "2014-01-01 오전 00:01:00"
  }

  "#withSeconds" should "날짜의 초를 설정한다" in {
    getDate(2014, 1, 1, 0, 0, 0, 0).withSeconds(1).format("yyyy-MM-dd a HH:mm:ss", locale = Locale.KOREA) shouldBe "2014-01-01 오전 00:00:01"
  }

  "#withMilliseconds" should "날짜의 밀리초를 설정한다" in {
    getDate(2014, 1, 1, 0, 0, 0, 0).withMilliseconds(1).format("yyyy-MM-dd a HH:mm:ss SSS", locale = Locale.KOREA) shouldBe "2014-01-01 오전 00:00:00 001"
  }

  "#truncateYear" should "날짜를 년도까지를 제외하고 모두 초기화" in {
    getDate(2014, 7, 27, 13, 12, 7, 123).truncateYear.format("yyyy-MM-dd a HH:mm:ss", locale = Locale.KOREA) shouldBe "2014-01-01 오전 00:00:00"
  }

  "#truncateMonth" should "날짜를 월까지를 제외하고 모두 초기화" in {
    getDate(2014, 7, 27, 13, 12, 7, 123).truncateMonth.format("yyyy-MM-dd a HH:mm:ss", locale = Locale.KOREA) shouldBe "2014-07-01 오전 00:00:00"
  }

  "#truncateSemiMonth" should "날짜를 보름까지를 제외하고 모두 초기화" in {
    getDate(2014, 7, 27, 13, 12, 7, 123).truncateSemiMonth.format("yyyy-MM-dd a HH:mm:ss", locale = Locale.KOREA) shouldBe "2014-07-16 오전 00:00:00"
  }

  "#truncateDate" should "날짜를 일자까지를 제외하고 모두 초기화" in {
    getDate(2014, 7, 27, 13, 12, 7, 123).truncateDate.format("yyyy-MM-dd a HH:mm:ss", locale = Locale.KOREA) shouldBe "2014-07-27 오전 00:00:00"
  }

  "#truncateAmPm" should "날짜를 오전/오후까지를 제외하고 모두 초기화" in {
    getDate(2014, 7, 27, 13, 12, 7, 123).truncateAmPm.format("yyyy-MM-dd a HH:mm:ss", locale = Locale.KOREA) shouldBe "2014-07-27 오후 12:00:00"
  }

  "#truncateHour" should "날짜를 시간까지를 제외하고 모두 초기화" in {
    getDate(2014, 7, 27, 13, 12, 7, 123).truncateHour.format("yyyy-MM-dd a HH:mm:ss", locale = Locale.KOREA) shouldBe "2014-07-27 오후 13:00:00"
  }

  "#truncateMinute" should "날짜를 분까지를 제외하고 모두 초기화" in {
    getDate(2014, 7, 27, 13, 12, 7, 123).truncateMinute.format("yyyy-MM-dd a HH:mm:ss", locale = Locale.KOREA) shouldBe "2014-07-27 오후 13:12:00"
  }

  "#truncateSecond" should "날짜를 초까지를 제외하고 모두 초기화" in {
    getDate(2014, 7, 27, 13, 12, 7, 123).truncateSecond.format("yyyy-MM-dd a HH:mm:ss", locale = Locale.KOREA) shouldBe "2014-07-27 오후 13:12:07"
  }

  "#truncateMillisecond" should "날짜를 밀리초까지를 제외하고 모두 초기화" in {
    getDate(2014, 7, 27, 13, 12, 7, 123).truncateMillisecond.format("yyyy-MM-dd a HH:mm:ss SSS", locale = Locale.KOREA) shouldBe "2014-07-27 오후 13:12:07 123"
  }

  "#diff" should "두 날짜의 차이 계산" in {
    getDate(2014, 7, 27, 0, 0, 0, 0) diff getDate(2014, 7, 28, 0, 0, 0, 0) shouldBe 1.day
  }

  "#endTimeInDay" should "주어진 날짜의 만료시간(23시 59분 59초 999밀리초)" in {
    getDate(2014, 7, 27, 0, 0, 0, 0).endTimeInDay.format("yyyy-MM-dd a HH:mm:ss SSS", locale = Locale.KOREA) shouldBe "2014-07-27 오후 23:59:59 999"
  }
}
