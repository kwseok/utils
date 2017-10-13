package com.github.stonexx.slick.ext.util

import com.github.stonexx.scala.data.{OrderedEnumeration, Ordering}
import slick.lifted.Rep

object OrderedEnumerationUtils {

  def toSlickOrdered[O <: OrderedEnumeration](o: O#Ordered)(f: O#Value => Rep[_]) = new slick.lifted.Ordered(o.values.map {
    case (v, ord) => (f(v).toNode, slick.ast.Ordering(
      direction = ord.direction match {
        case Ordering.Asc => slick.ast.Ordering.Asc
        case Ordering.Desc => slick.ast.Ordering.Desc
      },
      nulls = ord.nulls match {
        case Ordering.NullsDefault => slick.ast.Ordering.NullsDefault
        case Ordering.NullsFirst => slick.ast.Ordering.NullsFirst
        case Ordering.NullsLast => slick.ast.Ordering.NullsLast
      }
    ))
  })
}
