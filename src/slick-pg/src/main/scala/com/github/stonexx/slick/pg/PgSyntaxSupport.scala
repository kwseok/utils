package com.github.stonexx.slick.pg

import slick.jdbc.PostgresProfile

trait PgSyntaxSupport { self: PostgresProfile =>

  // alias
  trait SyntaxImplicits extends SimpleSyntaxImplicits
  type SyntaxAssistants = syntax.SyntaxAssistants

  trait SimpleSyntaxImplicits extends syntax.ExtensionMethodConversions
}
