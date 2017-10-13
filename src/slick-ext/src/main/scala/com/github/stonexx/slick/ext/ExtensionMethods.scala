package com.github.stonexx.slick.ext

import com.github.stonexx.scala.data.OrderedEnumeration
import com.github.stonexx.slick.ext.exceptions._
import com.github.stonexx.slick.ext.util._
import slick.ast._, Library.SqlFunction
import slick.basic.BasicStreamingAction
import slick.dbio._
import slick.jdbc._
import slick.lifted._, FunctionSymbolExtensionMethods._

import scala.concurrent.ExecutionContext

object ExtensionLibrary {
  val Apply = new SqlFunction("") {
    override def toString = "Function ()"
  }
}

trait ColumnExtensionMethods[B1, P1] extends Any with ExtensionMethods[B1, P1] {
  def ordered(desc: Boolean): ColumnOrdered[P1] =
    ColumnOrdered(c, Ordering(direction = if (desc) Ordering.Desc else Ordering.Asc))
}

final class BaseColumnExtensionMethods[B1](val c: Rep[B1]) extends AnyVal with ColumnExtensionMethods[B1, B1] with BaseExtensionMethods[B1]

final class OptionColumnExtensionMethods[B1](val c: Rep[Option[B1]]) extends AnyVal with ColumnExtensionMethods[B1, Option[B1]] with OptionExtensionMethods[B1]

final class QueryExtensionMethods[E, U, C[_]](val q: Query[E, U, C]) extends AnyVal {
  def takeIfPositive(size: Long): Query[E, U, C] = size max 0 match {
    case 0 => q
    case s => q.take(s)
  }

  def dropIfPositive(size: Long): Query[E, U, C] = size max 0 match {
    case 0 => q
    case s => q.drop(s)
  }

  def limit(page: Int, size: Int): Query[E, U, C] = (page max 1, size max 0) match {
    case (_, 0) => q
    case (1, s) => q.take(s)
    case (p, s) => q.drop((p - 1) * s).take(s)
  }

  def filterByCondition[F](f: F)(implicit fc: QueryFilterCondition[E, F]): Query[E, U, C] =
    fc.condition(f)(QueryFilter())(q)

  def sortByCondition[S](s: S)(implicit sc: QuerySortCondition[E, S]): Query[E, U, C] =
    q.sortBy(sc.condition(s))
}

final class SingleColumnQueryExtensionMethods[B1, P1, C[_]](val q: Query[Rep[P1], _, C]) extends AnyVal {
  def head(implicit tm: TypedType[B1]): Rep[B1] = ExtensionLibrary.Apply.column[B1](q.toNode)
  def headOption(implicit tm: TypedType[Option[B1]]): Rep[Option[B1]] = ExtensionLibrary.Apply.column[Option[B1]](q.toNode)
}

final class DBIOActionExtensionMethods[R, S <: NoStream, E <: Effect](val action: DBIOAction[R, S, E]) extends AnyVal {
  def mustAffectOneSingleRow[E2 <: Effect](implicit ev: R <:< Int, ec: ExecutionContext): DBIOAction[R, NoStream, E with E2] = action.flatMap {
    case 1 => action
    case 0 => DBIOAction.failed(NoRowsAffectedException)
    case n if n > 1 => DBIOAction.failed(new TooManyRowsAffectedException(affectedRowCount = n, expectedRowCount = 1))
  }

  def mustAffectAtLeastOneRow[E2 <: Effect](implicit ev: R <:< Int, ec: ExecutionContext): DBIOAction[R, NoStream, E with E2] = action.flatMap {
    case n if n >= 1 => action
    case 0 => DBIOAction.failed(NoRowsAffectedException)
  }

  def mustSelectSingleRecord[T, E2 <: Effect](implicit ev: R <:< Seq[T], ec: ExecutionContext): DBIOAction[T, NoStream, E with E2] = action.flatMap {
    case s if s.length == 1 => DBIOAction.successful(s.head)
    case s if s.isEmpty => DBIOAction.failed(NoRowsAffectedException)
    case s => DBIOAction.failed(new TooManyRowsAffectedException(affectedRowCount = s.size, expectedRowCount = 1))
  }

  def asStreaming[T](implicit ev: R <:< Seq[T]): DBIOAction[R, Streaming[T], Effect.Read] = (action match {
    case a: BasicStreamingAction[_, _, _] => a
    case _ => DBIOAction.failed(UnsupportedStreamingException)
  }).asInstanceOf[DBIOAction[R, Streaming[T], Effect.Read]]
}

final class SQLActionBuilderExtensionMethods(val builder: SQLActionBuilder) extends AnyVal {
  def ++(other: SQLActionBuilder): SQLActionBuilder = SQLActionBuilder(builder.queryParts ++ other.queryParts, new SetParameter[Unit] {
    def apply(p: Unit, pp: PositionedParameters): Unit = {
      builder.unitPConv.apply(p, pp)
      other.unitPConv.apply(p, pp)
    }
  })
  def ++(opt: Option[SQLActionBuilder]): SQLActionBuilder = opt.map(++).getOrElse(builder)
}

final class OrderedEnumerationExtensionMethods[O <: OrderedEnumeration](val o: O#Ordered) extends AnyVal {
  @inline def toSlickOrdered(f: O#Value => Rep[_]): slick.lifted.Ordered = OrderedEnumerationUtils.toSlickOrdered(o)(f)
}

trait ExtensionMethodConversions {
  implicit def extColumnExtensionMethods[B1: BaseTypedType](c: Rep[B1]): BaseColumnExtensionMethods[B1] = new BaseColumnExtensionMethods[B1](c)
  implicit def extOptionColumnExtensionMethods[B1: BaseTypedType](c: Rep[Option[B1]]): OptionColumnExtensionMethods[B1] = new OptionColumnExtensionMethods[B1](c)
  implicit def extQueryExtensionMethods[E, U, C[_]](q: Query[E, U, C]): QueryExtensionMethods[E, U, C] = new QueryExtensionMethods[E, U, C](q)
  implicit def extSingleColumnQueryExtensionMethods[B1: BaseTypedType, C[_]](q: Query[Rep[B1], _, C]): SingleColumnQueryExtensionMethods[B1, B1, C] = new SingleColumnQueryExtensionMethods[B1, B1, C](q)
  implicit def extSingleOptionColumnQueryExtensionMethods[B1: BaseTypedType, C[_]](q: Query[Rep[Option[B1]], _, C]): SingleColumnQueryExtensionMethods[B1, Option[B1], C] = new SingleColumnQueryExtensionMethods[B1, Option[B1], C](q)

  implicit def extDBIOActionExtensionMethods[R, S <: NoStream, E <: Effect](a: DBIOAction[R, S, E]): DBIOActionExtensionMethods[R, S, E] = new DBIOActionExtensionMethods[R, S, E](a)
  implicit def extSQLActionBuilderExtensionMethods(b: SQLActionBuilder): SQLActionBuilderExtensionMethods = new SQLActionBuilderExtensionMethods(b)

  implicit def extOrderedEnumerationExtensionMethods[E <: OrderedEnumeration](o: E#Ordered): OrderedEnumerationExtensionMethods[E] = new OrderedEnumerationExtensionMethods[E](o)
}

object ExtensionMethodConversions extends ExtensionMethodConversions
