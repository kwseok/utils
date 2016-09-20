package com.github.stonexx.play.db.anorm

import java.lang.reflect.InvocationTargetException

import anorm._

import scala.language.experimental.macros
import scala.reflect.macros.whitebox
import scala.reflect.runtime.{universe => ru}

trait EnumerationExtensions {
  object ToId {
    implicit def enumToParameterValue[E <: Enumeration : ru.TypeTag](enumValue: E#Value): ParameterValue = enumValue.id
    implicit def rowToEnumValue[E <: Enumeration]: Column[E#Value] = macro EnumerationExtensionsMacroImpl.rowToEnumValueById[E]
  }

  object ToName {
    implicit def enumToParameterValue[E <: Enumeration : ru.TypeTag](enumValue: E#Value): ParameterValue = enumValue.toString
    implicit def rowToEnumValue[E <: Enumeration]: Column[E#Value] = macro EnumerationExtensionsMacroImpl.rowToEnumValueByName[E]
  }
}

object EnumerationExtensionsMacroImpl {
  def rowToEnumValueById[E <: Enumeration](c: whitebox.Context)(implicit e: c.WeakTypeTag[E]): c.Expr[Column[E#Value]] = {
    import c.universe._
    val eTypeString = c.Expr[String](Literal(Constant(e.tpe.toString)))
    val columnNonNull1 = reify { (f: Int => E#Value) =>
      Column.nonNull { (value, meta) =>
        val MetaDataItem(qualified, _, _) = meta
        value match {
          case int: Int =>
            try Right(f(int))
            catch {
              case e: InvocationTargetException if e.getCause.isInstanceOf[NoSuchElementException] =>
                Left(SqlMappingError(s"Can't convert $int to ${eTypeString.splice}, because it isn't a valid value: $qualified"))
            }

          case _ => Left(TypeDoesNotMatch(s"Can't convert [$value:${value.asInstanceOf[AnyRef].getClass}] to ${eTypeString.splice}: $qualified"))
        }
      }
    }
    c.Expr[Column[E#Value]](q"$columnNonNull1(${e.tpe.termSymbol}.apply)")
  }

  def rowToEnumValueByName[E <: Enumeration](c: whitebox.Context)(implicit e: c.WeakTypeTag[E]): c.Expr[Column[E#Value]] = {
    import c.universe._
    val eTypeString = c.Expr[String](Literal(Constant(e.tpe.toString)))
    val columnNonNull1 = reify { (f: String => E#Value) =>
      Column.nonNull { (value, meta) =>
        val MetaDataItem(qualified, _, _) = meta
        value match {
          case string: String =>
            try Right(f(string))
            catch {
              case e: InvocationTargetException if e.getCause.isInstanceOf[NoSuchElementException] =>
                Left(SqlMappingError(s"Can't convert '$string' to ${eTypeString.splice}, because it isn't a valid value: $qualified"))
            }

          case _ => Left(TypeDoesNotMatch(s"Can't convert [$value:${value.asInstanceOf[AnyRef].getClass}] to ${eTypeString.splice}: $qualified"))
        }
      }
    }
    c.Expr[Column[E#Value]](q"$columnNonNull1(${e.tpe.termSymbol}.withName)")
  }
}
