package com.github.stonexx.slick
package ext

import com.github.stonexx.scala.data.OrderedEnumeration
import com.github.stonexx.slick.ext.util.QueryFilter
import slick.jdbc._

trait ExtJdbcProfile extends JdbcProfile { self =>

  override val api: ExtAPI = new ExtAPI {}

  trait ExtAPI extends super.API with ExtensionMethodConversions {
    val MappedEnumColumnType: self.MappedEnumColumnType.type = self.MappedEnumColumnType
    val QueryExtensions     : self.QueryExtensions.type      = self.QueryExtensions
  }

  import self.api._

  object MappedEnumColumnType {
    def id(enum: Enumeration): BaseColumnType[enum.Value] = MappedColumnType.base[enum.Value, Int](_.id, enum(_))
    def name(enum: Enumeration): BaseColumnType[enum.Value] = MappedColumnType.base[enum.Value, String](_.toString, s => enum.withName(s))
  }

  object QueryExtensions {
    def filterCondition[E, F](f: F => QueryFilter[E] => QueryFilter[E]): QueryFilterCondition[E, F] = new QueryFilterCondition[E, F] {
      override def condition: F => QueryFilter[E] => QueryFilter[E] = f
    }

    def sortCondition[E, S](f: S => E => slick.lifted.Ordered): QuerySortCondition[E, S] = new QuerySortCondition[E, S] {
      override def condition: S => E => slick.lifted.Ordered = f
    }

    def orderCondition[E, O <: OrderedEnumeration](f: O#Value => E => Rep[_]): QuerySortCondition[E, O#Ordered] = new QuerySortCondition[E, O#Ordered] {
      override def condition: O#Ordered => E => slick.lifted.Ordered = o => e => o.toSlickOrdered(f(_)(e))
    }
  }
}

object ExtDerbyProfile extends ExtDerbyProfile
trait ExtDerbyProfile extends DerbyProfile with ExtJdbcProfile

object ExtSQLServerProfile extends ExtSQLServerProfile
trait ExtSQLServerProfile extends SQLServerProfile with ExtJdbcProfile

object ExtPostgresProfile extends ExtPostgresProfile
trait ExtPostgresProfile extends PostgresProfile with ExtJdbcProfile

object ExtH2Profile extends ExtH2Profile
trait ExtH2Profile extends H2Profile with ExtJdbcProfile

object ExtHsqldbProfile extends ExtHsqldbProfile
trait ExtHsqldbProfile extends HsqldbProfile with ExtJdbcProfile

object ExtSQLiteProfile extends ExtSQLiteProfile
trait ExtSQLiteProfile extends SQLiteProfile with ExtJdbcProfile

object ExtMySQLProfile extends ExtMySQLProfile
trait ExtMySQLProfile extends MySQLProfile with ExtJdbcProfile

object ExtDB2Profile extends ExtDB2Profile
trait ExtDB2Profile extends DB2Profile with ExtJdbcProfile

object ExtOracleProfile extends ExtOracleProfile
trait ExtOracleProfile extends OracleProfile with ExtJdbcProfile
