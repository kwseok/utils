package com.github.stonexx.play.mvc.binders

import com.github.stonexx.play.util.JodaFormatters._
import org.joda.time.{Period, DateTime, LocalDate, LocalTime}
import org.joda.time.format.{PeriodFormatter, DateTimeFormatter}
import play.api.mvc.{QueryStringBindable, PathBindable}

trait JodaBinders extends JodaPathBinders with JodaQueryStringBinders

trait JodaPathBinders {

  /**
   * Path binder for the [[org.joda.time.DateTime]] type.
   *
   * @param formatter a date formatter
   */
  def pathBindableJodaDateTime(formatter: DateTimeFormatter): PathBindable[DateTime] = new PathBindable.Parsing[DateTime](
    formatter.parseDateTime, _.toString(formatter),
    (key, e) => "Cannot parse parameter %s as DateTime: %s".format(key, e.getMessage)
  )

  /**
   * the ISO8601 implicit [[org.joda.time.DateTime]] path binder
   */
  implicit lazy val pathBindableJodaIsoDateTime: PathBindable[DateTime] = pathBindableJodaDateTime(isoDateTimeFormatter)

  /**
   * Path binder for the [[org.joda.time.LocalDate]] type.
   *
   * @param formatter a date formatter
   */
  def pathBindableJodaLocalDate(formatter: DateTimeFormatter): PathBindable[LocalDate] = new PathBindable.Parsing[LocalDate](
    formatter.parseDateTime _ andThen (_.toLocalDate), _.toString(formatter),
    (key, e) => "Cannot parse parameter %s as LocalDate: %s".format(key, e.getMessage)
  )

  /**
   * the ISO8601 implicit [[org.joda.time.LocalDate]] path binder
   */
  implicit lazy val pathBindableJodaIsoLocalDate: PathBindable[LocalDate] = pathBindableJodaLocalDate(isoDateFormatter)

  /**
   * Path binder for the [[org.joda.time.LocalTime]] type.
   *
   * @param formatter a date formatter
   */
  def pathBindableJodaLocalTime(formatter: DateTimeFormatter): PathBindable[LocalTime] = new PathBindable.Parsing[LocalTime](
    formatter.parseDateTime _ andThen (_.toLocalTime), _.toString(formatter),
    (key, e) => "Cannot parse parameter %s as LocalTime: %s".format(key, e.getMessage)
  )

  /**
   * the ISO8601 implicit [[org.joda.time.LocalTime]] path binder
   */
  implicit lazy val pathBindableJodaIsoLocalTime: PathBindable[LocalTime] = pathBindableJodaLocalTime(isoTimeFormatter)

  /**
   * Path binder for the [[org.joda.time.Period]] type.
   *
   * @param formatter a date formatter
   */
  def pathBindableJodaPeriod(formatter: PeriodFormatter): PathBindable[Period] = new PathBindable.Parsing[Period](
    formatter.parsePeriod, _.toString(formatter),
    (key, e) => "Cannot parse parameter %s as Period: %s".format(key, e.getMessage)
  )

  /**
   * the ISO8601 implicit [[org.joda.time.Period]] path binder
   */
  implicit lazy val pathBindableJodaIsoPeriod: PathBindable[Period] = pathBindableJodaPeriod(isoPeriodFormatter)
}

trait JodaQueryStringBinders {

  /**
   * QueryString binder for the [[org.joda.time.DateTime]] type.
   *
   * @param formatter a date formatter
   */
  def queryStringBindableJodaDateTime(formatter: DateTimeFormatter): QueryStringBindable[DateTime] = new QueryStringBindable.Parsing[DateTime](
    formatter.parseDateTime, _.toString(formatter),
    (key, e) => "Cannot parse parameter %s as DateTime: %s".format(key, e.getMessage)
  )

  /**
   * the ISO8601 implicit [[org.joda.time.DateTime]] query string binder
   */
  implicit lazy val queryStringBindableJodaIsoDateTime: QueryStringBindable[DateTime] = queryStringBindableJodaDateTime(isoDateTimeFormatter)

  /**
   * QueryString binder for the [[org.joda.time.LocalDate]] type.
   *
   * @param formatter a date formatter
   */
  def queryStringBindableJodaLocalDate(formatter: DateTimeFormatter): QueryStringBindable[LocalDate] = new QueryStringBindable.Parsing[LocalDate](
    formatter.parseDateTime _ andThen (_.toLocalDate), _.toString(formatter),
    (key, e) => "Cannot parse parameter %s as LocalDate: %s".format(key, e.getMessage)
  )

  /**
   * the ISO8601 implicit [[org.joda.time.LocalDate]] query string binder
   */
  implicit lazy val queryStringBindableJodaIsoLocalDate: QueryStringBindable[LocalDate] = queryStringBindableJodaLocalDate(isoDateFormatter)

  /**
   * QueryString binder for the [[org.joda.time.LocalTime]] type.
   *
   * @param formatter a date formatter
   */
  def queryStringBindableJodaLocalTime(formatter: DateTimeFormatter): QueryStringBindable[LocalTime] = new QueryStringBindable.Parsing[LocalTime](
    formatter.parseDateTime _ andThen (_.toLocalTime), _.toString(formatter),
    (key, e) => "Cannot parse parameter %s as LocalTime: %s".format(key, e.getMessage)
  )

  /**
   * the ISO8601 implicit [[org.joda.time.LocalTime]] query string binder
   */
  implicit lazy val queryStringBindableJodaIsoLocalTime: QueryStringBindable[LocalTime] = queryStringBindableJodaLocalTime(isoTimeFormatter)

  /**
   * QueryString binder for the [[org.joda.time.Period]] type.
   *
   * @param formatter a date formatter
   */
  def queryStringBindableJodaPeriod(formatter: PeriodFormatter): QueryStringBindable[Period] = new QueryStringBindable.Parsing[Period](
    formatter.parsePeriod, _.toString(formatter),
    (key, e) => "Cannot parse parameter %s as Period: %s".format(key, e.getMessage)
  )

  /**
   * the ISO8601 implicit [[org.joda.time.Period]] query string binder
   */
  implicit lazy val queryStringBindableJodaIsoPeriod: QueryStringBindable[Period] = queryStringBindableJodaPeriod(isoPeriodFormatter)
}
