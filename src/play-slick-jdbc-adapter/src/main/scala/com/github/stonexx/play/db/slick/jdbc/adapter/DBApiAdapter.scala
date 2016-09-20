package com.github.stonexx.play.db.slick.jdbc.adapter

import java.sql.{Connection, SQLException}
import javax.inject.Inject
import javax.sql.DataSource

import com.github.stonexx.play.db.slick.{SlickApi, Database}
import play.api.Logger
import play.api.db.{DBApi, Database => PlayDatabase}
import slick.jdbc.hikaricp.HikariCPJdbcDataSource
import slick.jdbc.DataSourceJdbcDataSource

class DBApiAdapter @Inject()(slickApi: SlickApi) extends DBApi {
  private lazy val databasesByName: Map[String, PlayDatabase] = slickApi.databases.map {
    case (name, db) => (name, new DBApiAdapter.DatabaseAdapter(name, db))
  }(collection.breakOut)

  override def databases: Seq[PlayDatabase] = databasesByName.values.toSeq

  override def database(name: String): PlayDatabase =
    databasesByName.getOrElse(name, throw new IllegalArgumentException(s"Could not find database for $name"))

  override def shutdown(): Unit = {
    // no-op: shutting down dbs is automatically managed by `slickApi`
    ()
  }
}

object DBApiAdapter {
  private class DatabaseAdapter(val name: String, db: Database) extends PlayDatabase {
    private val logger = Logger(classOf[DatabaseAdapter])

    def dataSource: DataSource = db.source match {
      case ds: DataSourceJdbcDataSource => ds.ds
      case hds: HikariCPJdbcDataSource => hds.ds
      case other =>
        logger.error(s"Unexpected data source type ${other.getClass}.")
        throw new UnsupportedOperationException
    }

    lazy val url: String = withConnection(_.getMetaData.getURL)

    def getConnection(): Connection = db.source.createConnection()

    def getConnection(autocommit: Boolean): Connection = {
      val conn = getConnection()
      conn.setAutoCommit(autocommit)
      conn
    }

    def withConnection[A](block: Connection => A): A = {
      val conn = getConnection()
      try block(conn)
      finally {
        try conn.close() catch {case _: SQLException =>}
      }
    }

    def withConnection[A](autocommit: Boolean)(block: Connection => A): A = withConnection { conn =>
      conn.setAutoCommit(autocommit)
      block(conn)
    }

    def withTransaction[A](block: Connection => A): A = {
      val conn = getConnection()
      var done = false
      try {
        val res = block(conn)
        conn.commit()
        done = true
        res
      } finally if (!done) conn.rollback()
    }

    def shutdown(): Unit = {
      // no-op. The rationale is that play-slick already takes care of closing the database on application shutdown
      ()
    }
  }
}
