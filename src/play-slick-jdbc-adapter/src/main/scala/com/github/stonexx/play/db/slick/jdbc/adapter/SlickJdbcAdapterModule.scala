package com.github.stonexx.play.db.slick.jdbc.adapter

import javax.inject.{Inject, Provider, Singleton}

import com.github.stonexx.play.db.slick.{SlickModule, SlickApi}
import play.api.db.{Database, DBApi}
import play.api.{Configuration, Environment}
import play.api.inject.{Module, Binding, BindingKey}
import play.db.NamedDatabaseImpl

/**
 * The Slick Jdbc Adapter module.
 */
@Singleton
class SlickJdbcAdapterModule extends Module {
  def bindings(env: Environment, config: Configuration): Seq[Binding[_]] = {
    val dbKey = config.underlying.getString(SlickModule.DbKeyConfig)
    val default = config.underlying.getString(SlickModule.DefaultDbName)
    val dbs = config.getOptional[Configuration](dbKey).getOrElse(Configuration.empty).subKeys
    Seq(
      bind[DBApi].to[DBApiAdapter].in[Singleton]
    ) ++ namedDatabaseBindings(dbs) ++ defaultDatabaseBinding(default, dbs)
  }

  private def bindNamed(name: String): BindingKey[Database] = {
    bind[Database].qualifiedWith(new NamedDatabaseImpl(name))
  }

  private def namedDatabaseBindings(dbs: Set[String]): Seq[Binding[_]] = dbs.toSeq.map { db =>
    bindNamed(db).to(new NamedDatabaseProvider(db))
  }

  private def defaultDatabaseBinding(default: String, dbs: Set[String]): Seq[Binding[_]] = {
    if (dbs.contains(default)) Seq(bind[Database].to(bindNamed(default))) else Nil
  }
}

/**
 * Helper to provide Slick implementation of DB API.
 */
trait SlickJdbcAdapterComponents {
  def slickApi: SlickApi

  lazy val dbApi: DBApi = new DBApiAdapter(slickApi)
}

/**
 * Inject provider for named databases.
 */
class NamedDatabaseProvider(name: String) extends Provider[Database] {
  //noinspection VarCouldBeVal
  @Inject private var dbApi: DBApi = _

  lazy val get: Database = dbApi.database(name)
}
