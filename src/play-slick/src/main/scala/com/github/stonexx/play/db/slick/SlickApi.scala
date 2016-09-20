package com.github.stonexx.play.db.slick

import com.typesafe.config.Config
import play.api._

import scala.util.control.NonFatal
import scala.util.{Success, Failure, Try}

/**
 * Slick API for managing application databases.
 */
trait SlickApi {

  /**
   * All configured databases.
   */
  def databases: Map[String, Database]

  /**
   * Get database with given configuration name.
   *
   * @param name the configuration name of the database
   */
  def database(name: String): Database

  /**
   * Shutdown all databases, releasing resources.
   */
  def shutdown(): Unit

}

/**
 * Default implementation of the Slick API.
 */
class DefaultSlickApi(configuration: Map[String, Config], environment: Environment) extends SlickApi {
  private val logger = Logger(classOf[DefaultSlickApi])

  lazy val databases: Map[String, Database] = configuration.map {
    case (name, config) =>
      try name -> Database.forConfig(path = "", config)
      catch {
        case NonFatal(t) =>
          logger.error(s"Failed to create Slick database for key $name.", t)
          throw Configuration(config).reportError(name, s"Cannot connect to database [$name]", Some(t))
      }
  }

  def database(name: String): Database =
    databases.getOrElse(name, throw new PlayException("Could not find database configuration for ", name))

  /**
   * Try to connect to all data sources.
   */
  def connect(logConnection: Boolean = false): Unit = databases foreach {
    case (name, db) => try {
      val conn = db.source.createConnection()
      if (logConnection) logger.info(s"Database [$name] connected at ${conn.getMetaData.getURL}")
      conn.close()
    } catch {
      case NonFatal(e) =>
        throw Configuration(configuration(name)).reportError("url", s"Cannot connect to database [$name]", Some(e))
    }
  }

  def shutdown(): Unit = for ((name, db) <- databases) {
    Try(db.close()) match {
      case Success(_) => logger.debug(s"Database $name was successfully closed.")
      case Failure(e) => logger.warn(s"Error occurred while closing database $name.", e)
    }
  }
}
