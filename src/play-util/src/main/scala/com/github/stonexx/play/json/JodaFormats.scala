package com.github.stonexx.play.json

import com.github.stonexx.play.util.JodaFormatters._
import org.joda.time.{Period, DateTime, LocalDate, LocalTime}
import org.joda.time.format._
import play.api.data.validation.ValidationError
import play.api.libs.json._

trait JodaFormats {

  /**
   * Reads for the [[org.joda.time.DateTime]] type.
   *
   * @param formatter a date formatter
   * @param corrector a simple string transformation function that can be used to transform input String before parsing. Useful when standards are not exactly respected and require a few tweaks
   */
  def jodaDateReads(formatter: DateTimeFormatter, corrector: String => String = identity) = new Reads[DateTime] {
    private def parseDate(input: String): Either[Throwable, DateTime] =
      scala.util.control.Exception.allCatch[DateTime] either formatter.parseDateTime(input)

    def reads(json: JsValue): JsResult[DateTime] = json match {
      case JsNumber(d) => JsSuccess(new DateTime(d.toLong))
      case JsString(s) => parseDate(corrector(s)) match {
        case Right(d) => JsSuccess(d)
        case Left(e) => JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.jodadate.format", e.getMessage))))
      }
      case _ => JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.date"))))
    }
  }

  /**
   * the ISO8601 implicit [[org.joda.time.DateTime]] reads
   */
  implicit lazy val jodaIsoDateReads: Reads[DateTime] = jodaDateReads(isoDateTimeFormatter)

  /**
   * Reads for the [[org.joda.time.LocalDate]] type.
   *
   * @param formatter a date formatter
   * @param corrector string transformation function
   */
  def jodaLocalDateReads(formatter: DateTimeFormatter, corrector: String => String = identity) = new Reads[LocalDate] {
    private def parseDate(input: String): Either[Throwable, LocalDate] =
      scala.util.control.Exception.allCatch[LocalDate] either formatter.parseDateTime(input).toLocalDate

    def reads(json: JsValue): JsResult[LocalDate] = json match {
      case JsString(s) => parseDate(corrector(s)) match {
        case Right(d) => JsSuccess(d)
        case Left(e) => JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.jodadate.format", e.getMessage))))
      }
      case _ => JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.date"))))
    }
  }

  /**
   * the ISO8601 implicit [[org.joda.time.LocalDate]] reads
   */
  implicit lazy val jodaIsoLocalDateReads: Reads[LocalDate] = jodaLocalDateReads(isoDateFormatter)

  /**
   * Reads for the [[org.joda.time.LocalTime]] type.
   *
   * @param formatter a date formatter
   * @param corrector string transformation function
   */
  def jodaLocalTimeReads(formatter: DateTimeFormatter, corrector: String => String = identity): Reads[LocalTime] = new Reads[LocalTime] {
    private def parseTime(input: String): Either[Throwable, LocalTime] =
      scala.util.control.Exception.allCatch[LocalTime] either formatter.parseDateTime(input).toLocalTime

    def reads(json: JsValue): JsResult[LocalTime] = json match {
      case JsNumber(n) => JsSuccess(new LocalTime(n.toLong))
      case JsString(s) => parseTime(corrector(s)) match {
        case Right(t) => JsSuccess(t)
        case Left(e) => JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.jodatime.format", e.getMessage))))
      }
      case _ => JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.time"))))
    }
  }

  /**
   * the ISO8601 implicit [[org.joda.time.LocalTime]] reads
   */
  implicit lazy val jodaIsoLocalTimeReads: Reads[LocalTime] = jodaLocalTimeReads(isoTimeFormatter)

  /**
   * Reads for the [[org.joda.time.Period]] type.
   *
   * @param formatter a date formatter
   * @param corrector string transformation function
   */
  def jodaPeriodReads(formatter: PeriodFormatter, corrector: String => String = identity) = new Reads[Period] {
    private def parsePeriod(input: String): Either[Throwable, Period] =
      scala.util.control.Exception.allCatch[Period] either formatter.parsePeriod(input)

    def reads(json: JsValue): JsResult[Period] = json match {
      case JsString(s) => parsePeriod(corrector(s)) match {
        case Right(t) => JsSuccess(t)
        case Left(e) => JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.jodaperiod.format", e.getMessage))))
      }
      case _ => JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.period"))))
    }
  }

  /**
   * the ISO8601 implicit [[org.joda.time.Period]] reads
   */
  implicit lazy val jodaIsoPeriodReads: Reads[Period] = jodaPeriodReads(isoPeriodFormatter)
}
