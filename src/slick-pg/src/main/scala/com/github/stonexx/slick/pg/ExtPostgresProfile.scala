package com.github.stonexx.slick.pg

trait ExtPostgresProfile
  extends com.github.tminglei.slickpg.ExPostgresProfile
    with com.github.stonexx.slick.ext.ExtPostgresProfile {

  override val api: API = new API {}

  trait API extends super.API with ExtAPI
}

object ExtPostgresProfile extends ExtPostgresProfile
