package com.github.stonexx.play.data
package forms

import com.github.stonexx.scala.data.Limit
import play.api.data.Forms.{optional, mapping}
import play.api.data.Mapping

trait LimitForms {

  def limit[T](implicit m: Mapping[T]): Mapping[Limit[T]] = mapping(
    "from" -> optional(m),
    "to" -> optional(m),
    "eq" -> optional(m)
  )(
    (from, to, eq) => Limit(from orElse eq, to orElse eq)
  )(
    limit => Option(limit.from, limit.to, limit.from.filter(limit.to.contains))
  )

}
