package com.github.stonexx.play.data.formats

import com.github.stonexx.scala.data.{SortableEnumeration, OrderedEnumeration}
import com.github.stonexx.scala.util.string._
import play.api.data.FormError
import play.api.data.format.Formatter

trait EnumFormats {

  def enumFormat[E <: Enumeration](
    parse: String => E#Value,
    serialize: E#Value => String,
    error: (String, Throwable) => Seq[FormError]
  ): Formatter[E#Value] = new Formatter[E#Value] {
    def bind(key: String, data: Map[String, String]) =
      play.api.data.format.Formats.stringFormat.bind(key, data).right.flatMap { s =>
        scala.util.control.Exception.allCatch[E#Value].either(parse(s)).left.map(error(key, _))
      }

    def unbind(key: String, value: E#Value) = Map(key -> serialize(value))
  }

  def enumIdFormat[E <: Enumeration](enum: E): Formatter[E#Value] = enumFormat(
    s => enum(s.toInt), _.id.toString,
    (key, _) => Seq(FormError(key, "error.enum.id", Nil))
  )

  def enumNameFormat[E <: Enumeration](enum: E): Formatter[E#Value] = enumFormat(
    s => enum.values.find(_.toString.camelToUnderscore == s.camelToUnderscore).get, _.toString,
    (key, _) => Seq(FormError(key, "error.enum.name", Nil))
  )

  def enumSortFormat[E <: SortableEnumeration](enum: E): Formatter[E#Ordered] = new Formatter[E#Ordered] {
    def bind(key: String, data: Map[String, String]) =
      play.api.data.format.Formats.stringFormat.bind(key, data).right.flatMap { s =>
        scala.util.control.Exception.allCatch[E#Ordered].either(enum.Ordered.parse(s))
          .left.map(_ => Seq(FormError(key, "error.sort", Seq(s))))
      }

    def unbind(key: String, value: E#Ordered) = Map(key -> value.toString)
  }

  def enumOrderedFormat[E <: OrderedEnumeration](enum: E): Formatter[E#Ordered] = new Formatter[E#Ordered] {
    def bind(key: String, data: Map[String, String]) =
      play.api.data.format.Formats.stringFormat.bind(key, data).right.flatMap { s =>
        scala.util.control.Exception.allCatch[E#Ordered].either(enum.Ordered.parse(s))
          .left.map(_ => Seq(FormError(key, "error.sort", Seq(s))))
      }

    def unbind(key: String, value: E#Ordered) = Map(key -> value.toString)
  }
}
