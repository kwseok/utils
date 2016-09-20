package com.github.stonexx.slick
package ext

import slick.jdbc._

trait ExtJdbcProfile extends JdbcProfile with PageableComponent { self =>

  override val api: ExtAPI = new ExtAPI {}

  trait ExtAPI extends super.API with ExtensionMethodConversions {
    type Paging[E, U] = self.Paging[E, U]
    type BasicPaging[E, U] = self.BasicPaging[E, U]
    type SimplePaging[E, U] = self.SimplePaging[E, U]
    val MappedEnumColumnType = self.MappedEnumColumnType
  }

  import self.api._

  object MappedEnumColumnType {
    def id(enum: Enumeration) = MappedColumnType.base[enum.Value, Int](_.id, enum(_))
    def name(enum: Enumeration) = MappedColumnType.base[enum.Value, String](_.toString, s => enum.withName(s))
  }
}

trait ExtDerbyProfile extends DerbyProfile with ExtJdbcProfile
object ExtDerbyProfile extends ExtDerbyProfile

trait ExtSQLServerProfile extends SQLServerProfile with ExtJdbcProfile
object ExtSQLServerProfile extends ExtSQLServerProfile

trait ExtPostgresProfile extends PostgresProfile with ExtJdbcProfile
object ExtPostgresProfile extends ExtPostgresProfile

trait ExtH2Profile extends H2Profile with ExtJdbcProfile
object ExtH2Profile extends ExtH2Profile

trait ExtHsqldbProfile extends HsqldbProfile with ExtJdbcProfile
object ExtHsqldbProfile extends ExtHsqldbProfile

trait ExtSQLiteProfile extends SQLiteProfile with ExtJdbcProfile
object ExtSQLiteProfile extends ExtSQLiteProfile

trait ExtMySQLProfile extends MySQLProfile with ExtJdbcProfile
object ExtMySQLProfile extends ExtMySQLProfile

trait ExtDB2Profile extends DB2Profile with ExtJdbcProfile
object ExtDB2Profile extends ExtDB2Profile

trait ExtOracleProfile extends OracleProfile with ExtJdbcProfile
object ExtOracleProfile extends ExtOracleProfile
