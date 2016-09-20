package com.github.stonexx.play.util

import org.joda.time.format._

object JodaFormatters {
  val isoDateTimeFormatter: DateTimeFormatter = new DateTimeFormatterBuilder().append(
    ISODateTimeFormat.dateTime.getPrinter,
    ISODateTimeFormat.dateTimeParser.getParser
  ).toFormatter

  val isoDateFormatter: DateTimeFormatter = new DateTimeFormatterBuilder().append(
    ISODateTimeFormat.date.getPrinter,
    ISODateTimeFormat.dateTimeParser.getParser
  ).toFormatter

  val isoTimeFormatter: DateTimeFormatter = new DateTimeFormatterBuilder().append(
    ISODateTimeFormat.time.getPrinter,
    ISODateTimeFormat.timeParser.getParser
  ).toFormatter

  val isoPeriodFormatter: PeriodFormatter = ISOPeriodFormat.standard
}
