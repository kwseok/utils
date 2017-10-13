package com.github.stonexx.slick.pg

import java.sql.{Date, Timestamp, Time}
import java.util.UUID

import com.github.tminglei.slickpg.PgArraySupport
import com.google.common.cache.CacheBuilder
import slick.jdbc.{PostgresProfile, JdbcType}

import scala.annotation.unchecked.{uncheckedVariance => uV}
import scala.collection.generic.CanBuildFrom
import scala.reflect.{ClassTag, classTag}
import scala.reflect.runtime.{universe => ru}

trait PgArraySupportFixed extends PgArraySupport { self: PostgresProfile =>
  import self.api._

  trait FixedArrayCodeGenSupport {
    self match {
      case p: com.github.tminglei.slickpg.ExPostgresProfile =>
        p.bindPgTypeToScala("_varchar", classTag[Seq[String]])
      case _ =>
    }
  }

  trait FixedArrayImplicits
    extends FixedArrayCodeGenSupport
      with SimpleArrayCodeGenSupport
      with LowPrioritySimpleArrayJdbcTypeImplicits
      with LowPriorityAdvancedArrayJdbcTypeImplicits
      with array.ExtensionMethodConversions {

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

    implicit def simpleArrayColumnExtensionMethods[B1, C[_]](c: Rep[C[B1]])(implicit tm: JdbcType[B1], tm1: JdbcType[C[B1]]) =
      new ArrayColumnExtensionMethods[B1, C, C[B1]](c)
    implicit def simpleArrayOptionColumnExtensionMethods[B1, C[_]](c: Rep[Option[C[B1]]])(implicit tm: JdbcType[B1], tm1: JdbcType[C[B1]]) =
      new ArrayColumnExtensionMethods[B1, C, Option[C[B1]]](c)
  }

  trait LowPrioritySimpleArrayJdbcTypeImplicits {
    private val simpleArrayTypeCache = CacheBuilder.newBuilder.maximumSize(500).build[ru.TypeTag[_], JdbcType[_]]

    implicit def simpleArrayJdbcTypeToJdbcType[T, C[X] <: Seq[X]](
      implicit
      t: SimpleArrayJdbcType[T],
      ct: ClassTag[C[T]], tt: ru.TypeTag[C[T]],
      cbf: CanBuildFrom[Nothing, T, C[T@uV]]
    ): JdbcType[C[T]] = {
      (if (ct == t.classTag) t else simpleArrayTypeCache.get(tt, t.to(_.to[C]))).asInstanceOf[JdbcType[C[T]]]
    }
  }

  trait LowPriorityAdvancedArrayJdbcTypeImplicits {
    private val advancedArrayTypeCache = CacheBuilder.newBuilder.maximumSize(500).build[ru.TypeTag[_], JdbcType[_]]

    implicit def advancedArrayJdbcTypeToJdbcType[T, C[X] <: Seq[X]](
      implicit
      t: AdvancedArrayJdbcType[T],
      ct: ClassTag[C[T]], tt: ru.TypeTag[C[T]],
      cbf: CanBuildFrom[Nothing, T, C[T@uV]]
    ): JdbcType[C[T]] = {
      (if (ct == t.classTag) t else advancedArrayTypeCache.get(tt, t.to(_.to[C]))).asInstanceOf[JdbcType[C[T]]]
    }
  }

  trait FixedArrayPlainImplicits extends FixedArrayCodeGenSupport with SimpleArrayPlainImplicits
}
