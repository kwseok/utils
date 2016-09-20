package com.github.stonexx.play.db

import play.api.Application

package object slick {
  type Database = _root_.slick.jdbc.JdbcBackend#Database
  val Database = _root_.slick.jdbc.JdbcBackend.Database

  private val slickApiCache = Application.instanceCache[SlickApi]

  def DB(name: String)(implicit app: Application): Database = slickApiCache(app).database(name)
  def DB(implicit app: Application): Database = DB(app.configuration.underlying.getString(SlickModule.DefaultDbName))
}
