package com.github.stonexx.scala.data

final case class Ordering(direction: Ordering.Direction = Ordering.Asc, nulls: Ordering.NullOrdering = Ordering.NullsDefault) {
  def asc: Ordering = copy(direction = Ordering.Asc)
  def desc: Ordering = copy(direction = Ordering.Desc)
  def reverse: Ordering = copy(direction = direction.reverse)
  def nullsDefault: Ordering = copy(nulls = Ordering.NullsDefault)
  def nullsFirst: Ordering = copy(nulls = Ordering.NullsFirst)
  def nullsLast: Ordering = copy(nulls = Ordering.NullsLast)
}

object Ordering {
  sealed abstract class NullOrdering(val name: String, val first: Boolean, val last: Boolean)
  case object NullsDefault extends NullOrdering("default", false, false)
  case object NullsFirst extends NullOrdering("first", true, false)
  case object NullsLast extends NullOrdering("last", false, true)

  sealed abstract class Direction(val name: String, val desc: Boolean) {def reverse: Direction}
  case object Asc extends Direction("asc", false) {def reverse = Desc}
  case object Desc extends Direction("desc", true) {def reverse = Asc}
}
