package com.github.stonexx.slick.pg

import slick.jdbc.PostgresProfile

trait PgStringSupport { self: PostgresProfile =>

  // alias
  trait StringImplicits extends SimpleStringImplicits

  trait SimpleStringImplicits extends string.ExtensionMethodConversions
}
