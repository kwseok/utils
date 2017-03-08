package com.github.stonexx.play.data
package forms

import com.github.stonexx.scala.data.OrderedEnumeration
import play.api.data.{Mapping, Forms}

trait EnumForms {
  import formats.enum._

  /**
   * Constructs a simple mapping for a text field (mapped as `scala.Enumeration`)
   *
   * For example:
   * {{{
   *   Form("status" -> enumId(Status))
   * }}}
   */
  def enumId[E <: Enumeration](enum: E): Mapping[E#Value] = Forms.of(enumIdFormat(enum))

  /**
   * Constructs a simple mapping for a text field (mapped as `scala.Enumeration`)
   *
   * For example:
   * {{{
   *   Form("status" -> enumName(Status))
   * }}}
   */
  def enumName[E <: Enumeration](enum: E): Mapping[E#Value] = Forms.of(enumNameFormat(enum))

  /**
   * Constructs a simple mapping for a text field (mapped as `scala.Enumeration`)
   *
   * For example:
   * {{{
   *   Form("status" -> enum(Status))
   * }}}
   */
  @inline def enum[E <: Enumeration](enum: E): Mapping[E#Value] = enumName(enum)

  /**
   * Constructs a simple mapping for a text field (mapped as `com.github.stonexx.scala.data.OrderedEnumeration`)
   *
   * For example:
   * {{{
   *   object Sorts extends com.github.stonexx.scala.data.OrderedEnumeration {
   *     ...
   *   }
   *   Form("sort" -> enumOrdered(Sorts))
   * }}}
   */
  def enumOrdered[E <: OrderedEnumeration](enum: E): Mapping[E#Ordered] = Forms.of(enumOrderedFormat(enum))
}
