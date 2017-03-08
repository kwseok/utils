package com.github.stonexx.play.db.slick.jdbc.adapter

import java.sql.Connection
import javax.inject.Inject
import javax.sql.DataSource

import com.github.stonexx.play.db.slick.{SlickApi, Database}
import play.api.Logger
import play.api.db.{DBApi, Database => PlayDatabase}
import slick.jdbc.hikaricp.HikariCPJdbcDataSource
import slick.jdbc.DataSourceJdbcDataSource

import scala.util.control.ControlThrowable

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

    // connection methods

    def getConnection(): Connection = {
      getConnection(autocommit = true)
    }

    def getConnection(autocommit: Boolean): Connection = {
      val connection = db.source.createConnection()
      connection.setAutoCommit(autocommit)
      connection
    }

    def withConnection[A](block: Connection => A): A = {
      withConnection(autocommit = true)(block)
    }

    def withConnection[A](autocommit: Boolean)(block: Connection => A): A = {
      val connection = getConnection(autocommit)
      try {
        block(connection)
      } finally {
        connection.close()
      }
    }

    def withTransaction[A](block: Connection => A): A = {
      withConnection(autocommit = false) { connection =>
        try {
          val r = block(connection)
          connection.commit()
          r
        } catch {
          case e: ControlThrowable =>
            connection.commit()
            throw e
          case e: Throwable =>
            connection.rollback()
            throw e
        }
      }
    }

    def shutdown(): Unit = {
      // no-op. The rationale is that play-slick already takes care of closing the database on application shutdown
      ()
    }
  }
}
