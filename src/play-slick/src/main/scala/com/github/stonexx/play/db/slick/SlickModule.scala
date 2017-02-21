package com.github.stonexx.play.db.slick

import javax.inject.{Inject, Provider, Singleton}

import com.typesafe.config.Config
import play.api.{Configuration, Mode, Environment}
import play.api.inject.{Module, Binding, BindingKey, ApplicationLifecycle}
import play.db.NamedDatabaseImpl

import scala.concurrent.Future
import scala.collection.JavaConverters._

object SlickModule {
  final val DbKeyConfig   = "play.slick.db.config"
  final val DefaultDbName = "play.slick.db.default"
}

/**
 * The Slick module.
 */
@Singleton
final class SlickModule extends Module {
  def bindings(env: Environment, config: Configuration): Seq[Binding[_]] = {
    val dbKey = config.underlying.getString(SlickModule.DbKeyConfig)
    val default = config.underlying.getString(SlickModule.DefaultDbName)
    val dbs = config.getOptional[Configuration](dbKey).getOrElse(Configuration.empty).subKeys
    Seq(
      bind[SlickApi].toProvider[SlickApiProvider]
    ) ++ namedDatabaseBindings(dbs) ++ defaultDatabaseBinding(default, dbs)
  }

  private def bindNamed(name: String): BindingKey[Database] =
    bind[Database].qualifiedWith(new NamedDatabaseImpl(name))

  private def namedDatabaseBindings(dbs: Set[String]): Seq[Binding[_]] = dbs.toSeq.map { db =>
    bindNamed(db).to(new NamedDatabaseProvider(db))
  }

  private def defaultDatabaseBinding(default: String, dbs: Set[String]): Seq[Binding[_]] =
    if (dbs.contains(default)) Seq(bind[Database].to(bindNamed(default))) else Nil
}

/**
 * The Slick components (for compile-time injection).
 */
trait SlickComponents {
  def environment: Environment
  def configuration: Configuration
  def applicationLifecycle: ApplicationLifecycle

  lazy val slickApi: SlickApi = new SlickApiProvider(environment, configuration, applicationLifecycle).get
}

/**
 * Inject provider for DB implementation of Slick API.
 */
@Singleton
class SlickApiProvider @Inject()(
  environment: Environment, configuration: Configuration,
  lifecycle: ApplicationLifecycle
) extends Provider[SlickApi] {
  lazy val get: SlickApi = {
    val config = configuration.underlying
    val dbKey = config.getString(SlickModule.DbKeyConfig)
    val configs = if (config.hasPath(dbKey)) {
      val obj = config.getObject(dbKey)
      val conf = obj.toConfig
      obj.keySet.asScala.map { key => key -> conf.getConfig(key) }.toMap
    } else Map.empty[String, Config]
    val slickApi = new DefaultSlickApi(configs, environment)
    lifecycle.addStopHook { () => Future.successful(slickApi.shutdown()) }
    slickApi.connect(logConnection = environment.mode != Mode.Test)
    slickApi
  }
}

/**
 * Inject provider for named databases.
 */
class NamedDatabaseProvider(name: String) extends Provider[Database] {
  //noinspection VarCouldBeVal
  @Inject private var slickApi: SlickApi = _

  lazy val get: Database = slickApi.database(name)
}
