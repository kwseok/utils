package com.github.stonexx.scala.util

import com.google.common.reflect.TypeToken

import scala.language.experimental.macros
import scala.reflect.ClassTag
import scala.reflect.macros.whitebox
import scala.reflect.runtime.{universe => ru}

object TypeUtils {
  @inline def typeTag[T: ru.TypeTag](obj: T): ru.TypeTag[T] = ru.typeTag[T]
  @inline def typeTag[T: ru.TypeTag](cls: Class[T]): ru.TypeTag[T] = ru.typeTag[T]

  @inline def typeOf[T: ru.TypeTag](obj: T): ru.Type = ru.typeOf[T]
  @inline def typeOf[T: ru.TypeTag](cls: Class[T]): ru.Type = ru.typeOf[T]

  @inline def classTag[T: ClassTag](obj: T): ClassTag[T] = scala.reflect.classTag[T]
  @inline def classTag[T: ClassTag](cls: Class[T]): ClassTag[T] = scala.reflect.classTag[T]

  @inline def runtimeClass[T: ClassTag]: Class[T] = implicitly[ClassTag[T]].runtimeClass.asInstanceOf[Class[T]]

  def captureJavaType(typeLiteral: String): java.lang.reflect.Type = macro TypeUtilsMacroImpl.captureJavaType_impl
}

object TypeUtilsMacroImpl {
  def captureJavaType_impl(c: whitebox.Context)(typeLiteral: c.Expr[String]): c.Expr[java.lang.reflect.Type] = {
    import c.universe._
    val typeTokenTypeSymbol = typeOf[TypeToken[_]].typeSymbol
    val Literal(Constant(typeLiteralValue: String)) = typeLiteral.tree
    val q"type T = $typeLiteralTree" = c.parse(s"type T = $typeLiteralValue")
    c.Expr(q"new $typeTokenTypeSymbol[$typeLiteralTree]{}.getType")
  }
}
