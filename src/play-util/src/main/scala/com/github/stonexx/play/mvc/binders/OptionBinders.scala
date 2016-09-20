package com.github.stonexx.play.mvc.binders

import play.api.mvc._

trait OptionBinders {

  implicit def pathBindableOption[T: PathBindable]: PathBindable[Option[T]] = new PathBindable[Option[T]] {
    def bind(key: String, value: String): Either[String, Option[T]] = implicitly[PathBindable[T]].bind(key, value).fold(
      left => Left(left),
      right => Right(Some(right))
    )

    def unbind(key: String, value: Option[T]): String = value.map(implicitly[PathBindable[T]].unbind(key, _)).getOrElse("")
  }
}
