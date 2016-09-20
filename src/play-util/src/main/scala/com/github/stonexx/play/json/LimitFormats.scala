package com.github.stonexx.play.json

import com.github.stonexx.scala.data.Limit
import play.api.libs.functional.syntax._
import play.api.libs.json._

trait LimitFormats {

  // @formatter:off
  implicit def limitJsonFormat[A: Format]: Format[Limit[A]] = (
    (__ \ 'from).formatNullable[A] ~
    (__ \ 'to).formatNullable[A]
  )(Limit.apply[A], unlift(Limit.unapply))
  // @formatter:on
}
