package com.github.stonexx.scala.data

import com.github.stonexx.scala.util.string._

abstract class SortableEnumeration extends Enumeration { self =>

  final def withUnderscoreName(s: String): Value =
    values.find(_.toString.camelToUnderscore == s.camelToUnderscore)
      .getOrElse(throw new NoSuchElementException(s"No value found for '$s'"))

  case class Ordered(value: Value, desc: Boolean) {
    def reverse: Ordered = copy(desc = !desc)

    override def toString: String = Ordered.stringify(this)
  }

  object Ordered {
    def parse(s: String): Ordered = s.headOption match {
      case Some('+') => Ordered(withUnderscoreName(s.drop(1)), desc = false)
      case Some('-') => Ordered(withUnderscoreName(s.drop(1)), desc = true)
      case _ => Ordered(withUnderscoreName(s), desc = false)
    }

    def stringify(ord: Ordered): String = (if (ord.desc) "-" else "") + ord.value
  }

  implicit final class OrderedValue(val v: Value) {
    def ord(desc: Boolean): Ordered = Ordered(v, desc)
    def asc: Ordered = ord(false)
    def desc: Ordered = ord(true)
  }
}
