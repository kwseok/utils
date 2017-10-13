package com.github.stonexx.slick.ext.util

import com.github.stonexx.scala.data.OrderedEnumeration
import slick.lifted.Rep

trait QueryFilterCondition[E, F] {
  def condition: F => QueryFilter[E] => QueryFilter[E]
}

trait QuerySortCondition[E, S] {
  def condition: S => E => slick.lifted.Ordered
}

object QueryConditions {
  def filterCondition[E, F](f: F => QueryFilter[E] => QueryFilter[E]): QueryFilterCondition[E, F] = new QueryFilterCondition[E, F] {
    override def condition: F => QueryFilter[E] => QueryFilter[E] = f
  }

  def sortCondition[E, S](f: S => E => slick.lifted.Ordered): QuerySortCondition[E, S] = new QuerySortCondition[E, S] {
    override def condition: S => E => slick.lifted.Ordered = f
  }

  def orderCondition[E, O <: OrderedEnumeration](f: O#Value => E => Rep[_]): QuerySortCondition[E, O#Ordered] = new QuerySortCondition[E, O#Ordered] {
    override def condition: O#Ordered => E => slick.lifted.Ordered = o => e => OrderedEnumerationUtils.toSlickOrdered(o)(f(_)(e))
  }
}
