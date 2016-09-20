package com.github.stonexx.scala.util

import scala.collection.GenTraversableOnce

final class ProductOps(val self: Product) extends AnyVal {

  @inline def isDefinedAny: Boolean = self.productIterator.exists {
    case null | None => false
    case col: GenTraversableOnce[_] if col.isEmpty => false
    case arr: Array[_] if arr.isEmpty => false
    case _ => true
  }

  @inline def isEmptyAll: Boolean = !isDefinedAny
}

trait ToProductOps {
  implicit def toProductOps(x: Product): ProductOps = new ProductOps(x)
}
