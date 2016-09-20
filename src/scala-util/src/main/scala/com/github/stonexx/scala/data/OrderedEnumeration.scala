package com.github.stonexx.scala.data

import com.github.stonexx.scala.util.string._

abstract class OrderedEnumeration extends Enumeration { self =>

  final def withUnderscoreName(s: String): Value =
    values.find(_.toString.camelToUnderscore == s.camelToUnderscore)
      .getOrElse(throw new NoSuchElementException(s"No value found for '$s'"))

  class Ordered(val values: IndexedSeq[(Value, Ordering)]) {
    def +(other: Ordered): Ordered = new Ordered(values ++ other.values)

    def unary_! = new Ordered(values = values.map { case (v, o) => v -> o.reverse })

    override def equals(other: Any): Boolean = other match {
      case that: Ordered => values == that.values
      case _ => false
    }

    override def hashCode(): Int = Seq(values).map(_.hashCode).foldLeft(0)((a, b) => 31 * a + b)

    override def toString: String = Ordered.stringify(this)
  }

  object Ordered {
    def parse(s: String): Ordered = new Ordered(s.split(",").map(_.trim).filter(_.nonEmpty).map {
      _.split(":", 2) match {
        case Array(dirWithField, nulls@_*) =>
          val (dir, field) = dirWithField.headOption match {
            case Some('+') => Ordering.Asc -> withUnderscoreName(dirWithField.drop(1))
            case Some('-') => Ordering.Desc -> withUnderscoreName(dirWithField.drop(1))
            case _ => Ordering.Asc -> withUnderscoreName(dirWithField)
          }
          field -> Ordering(dir, nulls.headOption.map(_.toUpperCase) match {
            case Some("NF") => Ordering.NullsFirst
            case Some("NL") => Ordering.NullsLast
            case _ => Ordering.NullsDefault
          })
      }
    })

    def stringify(ord: Ordered): String = ord.values.map {
      case (v, o) =>
        val dir = o.direction match {
          case Ordering.Asc => ""
          case Ordering.Desc => "-"
        }
        val nulls = o.nulls match {
          case Ordering.NullsFirst => ":NF"
          case Ordering.NullsLast => ":NL"
          case Ordering.NullsDefault => ""
        }
        dir + v + nulls
    }.mkString(",")
  }

  case class ValueOrdered(value: Value, ord: Ordering) extends Ordered(Vector((value, ord))) {
    def asc = copy(ord = ord.asc)
    def desc = copy(ord = ord.desc)
    def reverse = copy(ord = ord.reverse)
    def nullsDefault = copy(ord = ord.nullsDefault)
    def nullsFirst = copy(ord = ord.nullsFirst)
    def nullsLast = copy(ord = ord.nullsLast)

    override def toString: String = Ordered.stringify(this)
  }

  implicit def value2ordered(value: Value): ValueOrdered = ValueOrdered(value, Ordering())
}
