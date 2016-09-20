package com.github.stonexx.play.data
package forms

import org.joda.time.{Period, DateTime, LocalDate, LocalTime}
import org.joda.time.format.{PeriodFormatter, DateTimeFormatter}
import play.api.data.{FieldMapping, Mapping}

trait JodaForms {
  import formats.joda._

  /**
   * Constructs a simple mapping for a date field (mapped as [[org.joda.time.DateTime]] type).
   *
   * For example:
   * {{{
   *   val printer = ISODateTimeFormat.dateTime.getPrinter
   *   val parser = ISODateTimeFormat.dateTimeParser.getParser
   *   val formatter = new DateTimeFormatterBuilder().append(printer, parser).toFormatter
   *   Form("birthdate" -> jodaDate(formatter))
   * }}}
   *
   * @param formatter a date formatter
   */
  def jodaDate(formatter: DateTimeFormatter): Mapping[DateTime] = FieldMapping[DateTime]()(jodaDateTimeFormat(formatter))

  /**
   * Constructs a simple mapping for a ISO8601 date field (mapped as [[org.joda.time.DateTime]] type).
   *
   * For example:
   * {{{
   *   Form("birthdate" -> jodaIsoDate)
   * }}}
   */
  lazy val jodaIsoDate: Mapping[DateTime] = FieldMapping[DateTime]()(jodaIsoDateTimeFormat)

  /**
   * Constructs a simple mapping for a date field (mapped as [[org.joda.time.LocalDate]] type).
   *
   * For example:
   * {{{
   *   val printer = ISODateTimeFormat.date.getPrinter
   *   val parser = ISODateTimeFormat.dateTimeParser.getParser
   *   val formatter = new DateTimeFormatterBuilder().append(printer, parser).toFormatter
   *   Form("birthdate" -> jodaLocalDate(formatter))
   * }}}
   *
   * @param formatter a date formatter
   */
  def jodaLocalDate(formatter: DateTimeFormatter): Mapping[LocalDate] = FieldMapping[LocalDate]()(jodaLocalDateFormat(formatter))

  /**
   * Constructs a simple mapping for a ISO8601 date field (mapped as [[org.joda.time.LocalDate]] type).
   *
   * For example:
   * {{{
   *   Form("birthdate" -> jodaIsoLocalDate)
   * }}}
   */
  lazy val jodaIsoLocalDate: Mapping[LocalDate] = FieldMapping[LocalDate]()(jodaIsoLocalDateFormat)

  /**
   * Constructs a simple mapping for a date field (mapped as [[org.joda.time.LocalTime]] type).
   *
   * For example:
   * {{{
   *   val printer = ISODateTimeFormat.time.getPrinter
   *   val parser = ISODateTimeFormat.timeParser.getParser
   *   val formatter = new DateTimeFormatterBuilder().append(printer, parser).toFormatter
   *   Form("birthdate" -> jodaLocalTime(formatter))
   * }}}
   *
   * @param formatter a date formatter
   */
  def jodaLocalTime(formatter: DateTimeFormatter): Mapping[LocalTime] = FieldMapping[LocalTime]()(jodaLocalTimeFormat(formatter))

  /**
   * Constructs a simple mapping for a ISO8601 date field (mapped as [[org.joda.time.LocalTime]] type).
   *
   * For example:
   * {{{
   *   Form("birthdate" -> jodaIsoLocalTime)
   * }}}
   */
  lazy val jodaIsoLocalTime: Mapping[LocalTime] = FieldMapping[LocalTime]()(jodaIsoLocalTimeFormat)

  /**
   * Constructs a simple mapping for a date field (mapped as [[org.joda.time.Period]] type).
   *
   * For example:
   * {{{
   *   Form("period" -> jodaPeriod(ISOPeriodFormat.standard))
   * }}}
   *
   * @param formatter a date formatter
   */
  def jodaPeriod(formatter: PeriodFormatter): Mapping[Period] = FieldMapping[Period]()(jodaPeriodFormat(formatter))

  /**
   * Constructs a simple mapping for a ISO8601 date field (mapped as [[org.joda.time.Period]] type).
   *
   * For example:
   * {{{
   *   Form("period" -> jodaIsoPeriod)
   * }}}
   */
  lazy val jodaIsoPeriod: Mapping[Period] = FieldMapping[Period]()(jodaIsoPeriodFormat)
}
