package com.github.stonexx.slick.pg

import com.github.tminglei.slickpg.{PgEnumSupportUtils, PgEnumSupport}
import com.github.tminglei.slickpg.utils.SimpleArrayUtils
import slick.jdbc.{PostgresProfile, JdbcType}

import scala.reflect.ClassTag

trait PgEnumSupportFixed extends PgEnumSupport { self: PostgresProfile =>
  import PgEnumSupportUtils.sqlName

  def createEnumArrayJdbcType[T <: Enumeration](sqlEnumTypeName: String, enumObject: T, quoteName: Boolean = false) =
    createEnumArrayJdbcType[enumObject.Value](sqlEnumTypeName, _.toString, s => enumObject.withName(s), quoteName)

  def createEnumArrayJdbcType[T: ClassTag](sqlEnumTypeName: String, enumToString: (T => String), stringToEnum: (String => T), quoteName: Boolean) =
    new AdvancedArrayJdbcType[T](sqlName(sqlEnumTypeName, quoteName),
      fromString = s => SimpleArrayUtils.fromString(s1 => stringToEnum(s1))(s).orNull,
      mkString = v => SimpleArrayUtils.mkString[T](enumToString)(v)
    )

  def createEnumJdbcTypeWithArrayJdbcType[T <: Enumeration](sqlEnumTypeName: String, enumObject: T, quoteName: Boolean = false)(implicit tag: ClassTag[enumObject.Value]): (JdbcType[enumObject.Value], AdvancedArrayJdbcType[enumObject.Value]) = {
    createEnumJdbcTypeWithArrayJdbcType[enumObject.Value](sqlEnumTypeName, _.toString, s => enumObject.withName(s), quoteName)
  }

  def createEnumJdbcTypeWithArrayJdbcType[T: ClassTag](sqlEnumTypeName: String, enumToString: (T => String), stringToEnum: (String => T), quoteName: Boolean): (JdbcType[T], AdvancedArrayJdbcType[T]) = {
    (createEnumJdbcType(sqlEnumTypeName, enumToString, stringToEnum, quoteName), createEnumArrayJdbcType(sqlEnumTypeName, enumToString, stringToEnum, quoteName))
  }
}
