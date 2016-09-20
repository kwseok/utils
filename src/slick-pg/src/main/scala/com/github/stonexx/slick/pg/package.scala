package com.github.stonexx.slick

import java.util.concurrent.Callable

package object pg {
  implicit private[pg] def fn2Callable[R](f: => R): Callable[R] = new Callable[R] {
    override def call(): R = f
  }
}
