package com.github.stonexx.play.mvc.binders

import com.github.stonexx.scala.data.OrderedEnumeration
import com.github.stonexx.scala.util.string._
import play.api.mvc.{QueryStringBindable, PathBindable}

trait EnumBinders {

  def enumIdPathBindable[E <: Enumeration](enum: E): PathBindable[E#Value] = new PathBindable.Parsing[E#Value](
    value => enum(value.toInt), _.id.toString,
    (key, e) => "Cannot parse parameter %s as %s[%s]: %s".format(key, enum, classOf[Enumeration].getSimpleName, e.getMessage)
  )

  def enumIdQueryStringBindable[E <: Enumeration](enum: E): QueryStringBindable[E#Value] = new QueryStringBindable.Parsing[E#Value](
    value => enum(value.toInt), _.id.toString,
    (key, e) => "Cannot parse parameter %s as %s[%s]: %s".format(key, enum, classOf[Enumeration].getSimpleName, e.getMessage)
  )

  def enumNamePathBindable[E <: Enumeration](enum: E): PathBindable[E#Value] = new PathBindable.Parsing[E#Value](
    value => enum.values.find(_.toString.camelToUnderscore == value.camelToUnderscore).get, _.toString,
    (key, e) => "Cannot parse parameter %s as %s[%s]: %s".format(key, enum, classOf[Enumeration].getSimpleName, e.getMessage)
  )

  def enumNameQueryStringBindable[E <: Enumeration](enum: E): QueryStringBindable[E#Value] = new QueryStringBindable.Parsing[E#Value](
    value => enum.values.find(_.toString.camelToUnderscore == value.camelToUnderscore).get, _.toString,
    (key, e) => "Cannot parse parameter %s as %s[%s]: %s".format(key, enum, classOf[Enumeration].getSimpleName, e.getMessage)
  )

  def enumOrderedPathBindable[E <: OrderedEnumeration](enum: E): PathBindable[E#Ordered] = new PathBindable.Parsing[E#Ordered](
    enum.Ordered.parse(_), _.toString,
    (key, e) => "Cannot parse parameter %s as %s[%s]: %s".format(key, enum.getClass.getName, classOf[OrderedEnumeration].getSimpleName, e.getMessage)
  )

  def enumOrderedQueryStringBindable[E <: OrderedEnumeration](enum: E): QueryStringBindable[E#Ordered] = new QueryStringBindable.Parsing[E#Ordered](
    enum.Ordered.parse(_), _.toString,
    (key, e) => "Cannot parse parameter %s as %s[%s]: %s".format(key, enum.getClass.getName, classOf[OrderedEnumeration].getSimpleName, e.getMessage)
  )

  // aliases

  @inline def enumPathBindable[E <: Enumeration](enum: E): PathBindable[E#Value] = enumNamePathBindable(enum)
  @inline def enumQueryStringBindable[E <: Enumeration](enum: E): QueryStringBindable[E#Value] = enumNameQueryStringBindable(enum)
}
