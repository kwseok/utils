package com.github.stonexx.slick.pg

import java.sql.{Date, Timestamp, Time}
import java.util.UUID

import com.github.tminglei.slickpg.PgArraySupport
import com.google.common.cache.CacheBuilder
import slick.ast.{Library, TypedType}
import slick.jdbc.{PostgresProfile, JdbcType}
import slick.lifted.FunctionSymbolExtensionMethods._

import scala.annotation.unchecked.{uncheckedVariance => uV}
import scala.collection.generic.CanBuildFrom
import scala.reflect.{ClassTag, classTag}
import scala.reflect.runtime.{universe => ru}

trait PgArraySupportFixed extends PgArraySupport { self: PostgresProfile =>
  import self.api._

  trait FixedArrayImplicits extends LowPrioritySimpleArrayImplicits with LowPriorityVarcharArrayImplicits with array.ExtensionMethodConversions {
    /** for type/name, @see [[org.postgresql.core.Oid]] and [[org.postgresql.jdbc.TypeInfoCache]] */
    implicit val simpleUUIDArrayTypeMapper  : SimpleArrayJdbcType[UUID]      = new SimpleArrayJdbcType[UUID]("uuid")
    implicit val simpleStrArrayTypeMapper   : SimpleArrayJdbcType[String]    = new SimpleArrayJdbcType[String]("text")
    implicit val simpleLongArrayTypeMapper  : SimpleArrayJdbcType[Long]      = new SimpleArrayJdbcType[Long]("int8")
    implicit val simpleIntArrayTypeMapper   : SimpleArrayJdbcType[Int]       = new SimpleArrayJdbcType[Int]("int4")
    implicit val simpleShortArrayTypeMapper : SimpleArrayJdbcType[Short]     = new SimpleArrayJdbcType[Int]("int2").mapTo[Short](_.toShort, _.toInt)
    implicit val simpleFloatArrayTypeMapper : SimpleArrayJdbcType[Float]     = new SimpleArrayJdbcType[Float]("float4")
    implicit val simpleDoubleArrayTypeMapper: SimpleArrayJdbcType[Double]    = new SimpleArrayJdbcType[Double]("float8")
    implicit val simpleBoolArrayTypeMapper  : SimpleArrayJdbcType[Boolean]   = new SimpleArrayJdbcType[Boolean]("bool")
    implicit val simpleDateArrayTypeMapper  : SimpleArrayJdbcType[Date]      = new SimpleArrayJdbcType[Date]("date")
    implicit val simpleTimeArrayTypeMapper  : SimpleArrayJdbcType[Time]      = new SimpleArrayJdbcType[Time]("time")
    implicit val simpleTsArrayTypeMapper    : SimpleArrayJdbcType[Timestamp] = new SimpleArrayJdbcType[Timestamp]("timestamp")

    implicit def simpleArrayColumnExtensionMethods[B1, C[_]](c: Rep[C[B1]])(implicit tm: JdbcType[B1], tm1: JdbcType[C[B1]]) = new ArrayColumnExtensionMethods[B1, C, C[B1]](c)
    implicit def simpleArrayOptionColumnExtensionMethods[B1, C[_]](c: Rep[Option[C[B1]]])(implicit tm: JdbcType[B1], tm1: JdbcType[C[B1]]) = new ArrayColumnExtensionMethods[B1, C, Option[C[B1]]](c)
  }

  trait LowPrioritySimpleArrayImplicits extends LowPriorityAdvancedArrayImplicits {
    private val simpleArrayTypeCache = CacheBuilder.newBuilder.maximumSize(500).build[ru.TypeTag[_], JdbcType[_]]

    implicit def simpleArrayJdbcType[T, C[X] <: Seq[X]](
      implicit t: SimpleArrayJdbcType[T],
      ct: ClassTag[C[T]], tt: ru.TypeTag[C[T]],
      cbf: CanBuildFrom[Nothing, T, C[T@uV]]
    ): JdbcType[C[T]] = {
      (if (ct == t.classTag) t else simpleArrayTypeCache.get(tt, t.to(_.to[C]))).asInstanceOf[JdbcType[C[T]]]
    }
  }

  trait LowPriorityAdvancedArrayImplicits {
    private val advancedArrayTypeCache = CacheBuilder.newBuilder.maximumSize(500).build[ru.TypeTag[_], JdbcType[_]]

    implicit def advancedArrayJdbcType[T, C[X] <: Seq[X]](
      implicit t: AdvancedArrayJdbcType[T],
      ct: ClassTag[C[T]], tt: ru.TypeTag[C[T]],
      cbf: CanBuildFrom[Nothing, T, C[T@uV]]
    ): JdbcType[C[T]] = {
      (if (ct == t.classTag) t else advancedArrayTypeCache.get(tt, t.to(_.to[C]))).asInstanceOf[JdbcType[C[T]]]
    }
  }

  trait LowPriorityVarcharArrayImplicits { this: FixedArrayImplicits with LowPrioritySimpleArrayImplicits =>
    private val varcharArrayTypeMapper = new SimpleArrayJdbcType[String]("varchar")
    private val varcharArrayTypeCache  = CacheBuilder.newBuilder.maximumSize(50).build[ClassTag[_], JdbcType[_]]

    private def varcharArrayJdbcType[C[X] <: Seq[X]](implicit ct: ClassTag[C[String]], cbf: CanBuildFrom[Nothing, String, C[String@uV]]): JdbcType[C[String]] = {
      val t: JdbcType[_] = if (ct == varcharArrayTypeMapper.classTag)
        varcharArrayTypeMapper
      else
        varcharArrayTypeCache.get(ct, varcharArrayTypeMapper.to(_.to[C]))
      t.asInstanceOf[JdbcType[C[String]]]
    }

    trait StringArrayAsVarcharArrayColumn[P] {
      def v: P
      def varcharArrayType: TypedType[P]
      def asVarcharArrayColumn: LiteralColumn[P] = LiteralColumn(v)(varcharArrayType)
    }

    implicit final class BaseStringArrayAsVarcharArrayColumn[C[X] <: Seq[X]](val v: C[String])(
      implicit ct: ClassTag[C[String]], cbf: CanBuildFrom[Nothing, String, C[String@uV]]
    ) extends StringArrayAsVarcharArrayColumn[C[String]] {
      override def varcharArrayType = varcharArrayJdbcType[C]
    }

    implicit final class OptionStringArrayAsVarcharArrayColumn[C[X] <: Seq[X]](val v: Option[C[String]])(
      implicit ct: ClassTag[C[String]], cbf: CanBuildFrom[Nothing, String, C[String@uV]]
    ) extends StringArrayAsVarcharArrayColumn[Option[C[String]]] {
      override def varcharArrayType = varcharArrayJdbcType[C].optionType
    }

    trait StringArrayColumnAsTextOrVarcharArrayColumn[P] {
      def c: Rep[P]
      def textArrayType: TypedType[P]
      def varcharArrayType: TypedType[P]
      def asColumnOfTextArray = Library.Cast.column[P](c.toNode)(textArrayType)
      def asColumnOfVarcharArray = Library.Cast.column[P](c.toNode)(varcharArrayType)
    }

    implicit final class BaseStringArrayColumnAsTextOrVarcharArrayColumn[C[X] <: Seq[X], CC[_]](val c: Rep[CC[String]])(
      implicit ev1: CC[String] <:< C[String], ev2: TypedType[C[String]] =:= TypedType[CC[String]],
      ct: ClassTag[C[String]], tt: ru.TypeTag[C[String]], cbf: CanBuildFrom[Nothing, String, C[String@uV]]
    ) extends StringArrayColumnAsTextOrVarcharArrayColumn[CC[String]] {
      override def textArrayType = simpleArrayJdbcType[String, C]
      override def varcharArrayType = varcharArrayJdbcType[C]
    }

    implicit final class OptionStringArrayColumnAsTextOrVarcharArrayColumn[C[X] <: Seq[X], CC[_]](val c: Rep[Option[CC[String]]])(
      implicit ev1: CC[String] <:< C[String], ev2: TypedType[Option[C[String]]] =:= TypedType[Option[CC[String]]],
      ct: ClassTag[C[String]], tt: ru.TypeTag[C[String]], cbf: CanBuildFrom[Nothing, String, C[String@uV]]
    ) extends StringArrayColumnAsTextOrVarcharArrayColumn[Option[CC[String]]] {
      override def textArrayType = simpleArrayJdbcType[String, C].optionType
      override def varcharArrayType = varcharArrayJdbcType[C].optionType
    }
  }

  trait FixedArrayPlainImplicits extends SimpleArrayPlainImplicits {
    self match {
      case p: com.github.tminglei.slickpg.ExPostgresProfile =>
        p.bindPgTypeToScala("_varchar", classTag[Seq[String]])
      case _ =>
    }
  }
}
