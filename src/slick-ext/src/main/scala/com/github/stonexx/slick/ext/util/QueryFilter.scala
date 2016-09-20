package com.github.stonexx.slick.ext.util

import slick.ast.Library.{Or, SqlOperator, And}
import slick.ast.OptionApply
import slick.ast.ScalaBaseType._
import slick.lifted._
import slick.lifted.FunctionSymbolExtensionMethods._

class QueryFilter[E] private(private val conditions: IndexedSeq[E => Rep[Option[Boolean]]]) {
  type Self = QueryFilter[E]

  def this() = this(Vector.empty)

  def isEmpty: Boolean = conditions.isEmpty

  def nonEmpty: Boolean = !isEmpty

  private def lift[R](f: E => R)(implicit wt: CanBeQueryCondition[R]): E => Rep[Option[Boolean]] = { (e: E) =>
    import CanBeQueryCondition._
    wt match {
      case BooleanCanBeQueryCondition => LiteralColumn(Option(f(e).asInstanceOf[Boolean]))
      case BooleanColumnCanBeQueryCondition => Rep.forNode(OptionApply(f(e).toNode))(booleanType.optionType)
      case BooleanOptionColumnCanBeQueryCondition => f(e).asInstanceOf[Rep[Option[Boolean]]]
    }
  }

  private def reduce(op: SqlOperator, conditions: Seq[E => Rep[Option[Boolean]]]): Option[E => Rep[Option[Boolean]]] = conditions match {
    case Seq() => None
    case Seq(cond) => Some(cond)
    case conds => Some(e => op.column[Option[Boolean]](conds.map(_ (e).toNode): _*))
  }

  def fold[B](ifEmpty: => B)(f: (E => Rep[Option[Boolean]]) => B): B =
    if (isEmpty) ifEmpty else f(reduce(And, conditions).get)

  def apply(e: E): Option[Rep[Option[Boolean]]] = reduce(And, conditions).map(_ (e))

  def apply[U, C[_]](q: Query[E, U, C]): Query[E, U, C] = fold(q)(q.filter(_))

  def filter[R: CanBeQueryCondition](f: E => R): Self = new QueryFilter(conditions :+ lift(f))

  def filter(qf: QueryFilter[E]): Self = new QueryFilter(conditions ++ qf.conditions)

  def filter[V, R: CanBeQueryCondition](value: Option[V])(f: E => V => R): Self = value.map { v =>
    new QueryFilter(conditions :+ lift(f(_: E)(v)))
  }.getOrElse(this)

  def filter[C[_] <: TraversableOnce[_], V, R: CanBeQueryCondition](value: C[V])(f: E => C[V] => R): Self =
    if (value.nonEmpty) new QueryFilter(conditions :+ lift(f(_: E)(value))) else this

  def junction(op: SqlOperator, filters: (Self => Self)*): QueryFilter[E] =
    new QueryFilter(reduce(op, filters.flatMap(f => reduce(And, f(QueryFilter()).conditions))).toVector)

  /// aliases

  @inline def ~[R: CanBeQueryCondition](f: E => R): Self = filter(f)
  @inline def ~(qf: QueryFilter[E]): Self = filter(qf)
  @inline def ~[V, R: CanBeQueryCondition](value: Option[V])(f: E => V => R): Self = filter(value)(f)
  @inline def ~[S[_] <: TraversableOnce[_], V, R: CanBeQueryCondition](value: S[V])(f: E => S[V] => R): Self = filter(value)(f)

  @inline def conjunction(filters: (Self => Self)*): Self = junction(And, filters: _*)
  @inline def disjunction(filters: (Self => Self)*): Self = junction(Or, filters: _*)

  @inline def and(filters: (Self => Self)*): Self = conjunction(filters: _*)
  @inline def or(filters: (Self => Self)*): Self = disjunction(filters: _*)

  @inline def &&(filters: (Self => Self)*): Self = conjunction(filters: _*)
  @inline def ||(filters: (Self => Self)*): Self = disjunction(filters: _*)
}

object QueryFilter {
  def apply[E](): QueryFilter[E] = new QueryFilter()
}
