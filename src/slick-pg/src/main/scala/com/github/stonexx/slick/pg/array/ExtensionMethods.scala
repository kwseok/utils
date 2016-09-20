package com.github.stonexx.slick.pg.array

import slick.ast.Library.SqlFunction
import slick.ast.{BaseTypedType, TypedType}
import slick.lifted.{Rep, Query}
import slick.lifted.FunctionSymbolExtensionMethods._

object ArrayLibrary {
  val Array = new SqlFunction("array")
}

final class SingleColumnQueryExtensionMethods[B1, P1, C[_]](val q: Query[Rep[P1], _, C]) extends AnyVal {
  def array(implicit tm: TypedType[C[B1]]) = ArrayLibrary.Array.column[C[B1]](q.toNode)
}

trait ExtensionMethodConversions {
  implicit def pgArraySingleColumnQueryExtensionMethods[B1: BaseTypedType, C[_]](q: Query[Rep[B1], _, C]): SingleColumnQueryExtensionMethods[B1, B1, C] = new SingleColumnQueryExtensionMethods[B1, B1, C](q)
  implicit def pgArraySingleOptionColumnQueryExtensionMethods[B1: BaseTypedType, C[_]](q: Query[Rep[Option[B1]], _, C]): SingleColumnQueryExtensionMethods[B1, Option[B1], C] = new SingleColumnQueryExtensionMethods[B1, Option[B1], C](q)
}
