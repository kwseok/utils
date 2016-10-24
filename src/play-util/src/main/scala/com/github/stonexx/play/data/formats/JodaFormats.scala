package com.github.stonexx.play.data.formats

import com.github.stonexx.play.util.JodaFormatters._
import org.joda.time.{Period, DateTime, LocalDate, LocalTime}
import org.joda.time.format.{PeriodFormatter, DateTimeFormatter}
import play.api.data.FormError
import play.api.data.format.Formatter

trait JodaFormats {
  private[formats] def parsing[T](parse: String => T, errMsg: String, errArgs: Throwable => Seq[Any])(key: String, data: Map[String, String]): Either[Seq[FormError], T] = {
    play.api.data.format.Formats.stringFormat.bind(key, data).right.flatMap { s =>
      scala.util.control.Exception.allCatch[T]
        .either(parse(s))
        .left.map(e => Seq(FormError(key, errMsg, errArgs(e))))
    }
  }

  /**
   * Play's Formatter for the [[org.joda.time.DateTime]] type.
   *
   * @param formatter a date formatter
   */
  def jodaDateTimeFormat(formatter: DateTimeFormatter): Formatter[DateTime] = new Formatter[DateTime] {
    override val format = Some(("format.date", Nil))

    def bind(key: String, data: Map[String, String]): Either[Seq[FormError], DateTime] = parsing(
      formatter.parseDateTime,
      "error.date", e => Seq(e.getMessage)
    )(key, data)

    def unbind(key: String, value: DateTime) = Map(key -> value.toString(formatter))
  }

  /**
   * the ISO8601 implicit [[org.joda.time.DateTime]] play's formatter
   */
  implicit lazy val jodaIsoDateTimeFormat: Formatter[DateTime] = jodaDateTimeFormat(isoDateTimeFormatter)

  /**
   * Play's Formatter for the [[org.joda.time.LocalDate]] type.
   *
   * @param formatter a date formatter
   */
  def jodaLocalDateFormat(formatter: DateTimeFormatter): Formatter[LocalDate] = new Formatter[LocalDate] {
    override val format = Some(("format.date", Nil))

    def bind(key: String, data: Map[String, String]): Either[Seq[FormError], LocalDate] = parsing(
      formatter.parseDateTime _ andThen (_.toLocalDate),
      "error.date", e => Seq(e.getMessage)
    )(key, data)

    def unbind(key: String, value: LocalDate) = Map(key -> value.toString(formatter))
  }

  /**
   * the ISO8601 implicit [[org.joda.time.LocalDate]] play's formatter
   */
  implicit lazy val jodaIsoLocalDateFormat: Formatter[LocalDate] = jodaLocalDateFormat(isoDateFormatter)

  /**
   * Play's Formatter for the [[org.joda.time.LocalTime]] type.
   *
   * @param formatter a date formatter
   */
  def jodaLocalTimeFormat(formatter: DateTimeFormatter): Formatter[LocalTime] = new Formatter[LocalTime] {
    override val format = Some(("format.time", Nil))

    def bind(key: String, data: Map[String, String]): Either[Seq[FormError], LocalTime] = parsing(
      formatter.parseDateTime _ andThen (_.toLocalTime),
      "error.time", e => Seq(e.getMessage)
    )(key, data)

    def unbind(key: String, value: LocalTime) = Map(key -> value.toString(formatter))
  }

  /**
   * the ISO8601 implicit [[org.joda.time.LocalTime]] play's formatter
   */
  implicit lazy val jodaIsoLocalTimeFormat: Formatter[LocalTime] = jodaLocalTimeFormat(isoTimeFormatter)

  /**
   * Play's Formatter for the [[org.joda.time.Period]] type.
   *
   * @param formatter a date formatter
   */
  def jodaPeriodFormat(formatter: PeriodFormatter): Formatter[Period] = new Formatter[Period] {
    override val format = Some(("format.period", Nil))

    def bind(key: String, data: Map[String, String]): Either[Seq[FormError], Period] = parsing(
      formatter.parsePeriod,
      "error.period", e => Seq(e.getMessage)
    )(key, data)

    def unbind(key: String, value: Period) = Map(key -> value.toString(formatter))
  }

  /**
   * the ISO8601 implicit [[org.joda.time.Period]] play's formatter
   */
  implicit lazy val jodaIsoPeriodFormat: Formatter[Period] = jodaPeriodFormat(isoPeriodFormatter)
}
