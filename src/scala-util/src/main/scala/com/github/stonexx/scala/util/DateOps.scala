package com.github.stonexx.scala.util

import java.util.{Calendar, Date, Locale, TimeZone}
import java.util.concurrent.TimeUnit

import org.apache.commons.lang3.time.{DateFormatUtils, DateUtils}

import scala.concurrent.duration._

final class DateOps(val self: Date) extends AnyVal {
  import Calendar._

  @inline def format(
    pattern: String,
    timeZone: TimeZone = TimeZone.getDefault,
    locale: Locale = Locale.getDefault
  ): String = DateFormatUtils.format(self, pattern, timeZone, locale)

  @inline def addYears(amount: Int): Date = DateUtils.addYears(self, amount)
  @inline def addMonths(amount: Int): Date = DateUtils.addMonths(self, amount)
  @inline def addDays(amount: Int): Date = DateUtils.addDays(self, amount)
  @inline def addWeeks(amount: Int): Date = DateUtils.addWeeks(self, amount)
  @inline def addHours(amount: Int): Date = DateUtils.addHours(self, amount)
  @inline def addMinutes(amount: Int): Date = DateUtils.addMinutes(self, amount)
  @inline def addSeconds(amount: Int): Date = DateUtils.addSeconds(self, amount)
  @inline def addMilliseconds(amount: Int): Date = DateUtils.addMilliseconds(self, amount)

  @inline def withYears(amount: Int): Date = DateUtils.setYears(self, amount)
  @inline def withMonths(amount: Int): Date = DateUtils.setMonths(self, amount - 1)
  @inline def withDays(amount: Int): Date = DateUtils.setDays(self, amount)
  @inline def withHours(amount: Int): Date = DateUtils.setHours(self, amount)
  @inline def withMinutes(amount: Int): Date = DateUtils.setMinutes(self, amount)
  @inline def withSeconds(amount: Int): Date = DateUtils.setSeconds(self, amount)
  @inline def withMilliseconds(amount: Int): Date = DateUtils.setMilliseconds(self, amount)

  @inline def truncYear: Date = DateUtils.truncate(self, YEAR)
  @inline def truncMonth: Date = DateUtils.truncate(self, MONTH)
  @inline def truncSemiMonth: Date = DateUtils.truncate(self, DateUtils.SEMI_MONTH)
  @inline def truncDate: Date = DateUtils.truncate(self, DATE)
  @inline def truncAmPm: Date = DateUtils.truncate(self, AM_PM)
  @inline def truncHour: Date = DateUtils.truncate(self, HOUR)
  @inline def truncMinute: Date = DateUtils.truncate(self, MINUTE)
  @inline def truncSecond: Date = DateUtils.truncate(self, SECOND)
  @inline def truncMillisecond: Date = DateUtils.truncate(self, MILLISECOND)

  @inline def diff(to: Date): Duration = Duration(to.getTime - self.getTime, TimeUnit.MILLISECONDS)

  /**
   * 해당 날짜의 만료시간을 가져온다.
   *
   * @param timeZone the time zone.
   * @param locale   the locale.
   * @return 만료시간. (23시 59분 59초 999밀리초)
   */
  def ofExpiry(timeZone: TimeZone = TimeZone.getDefault, locale: Locale = Locale.getDefault): Date = {
    val c = Calendar.getInstance(timeZone, locale)
    c.setLenient(false)
    c.setTimeInMillis(self.getTime)
    c.set(HOUR_OF_DAY, 23)
    c.set(MINUTE, 59)
    c.set(SECOND, 59)
    c.set(MILLISECOND, 999)
    c.getTime
  }

  @inline def ofExpiry: Date = ofExpiry()
}

trait ToDateOps {
  implicit def toDateOps(x: Date): DateOps = new DateOps(x)
}
