package slick

import com.github.tminglei.slickpg._
import com.github.stonexx.slick.pg._

trait MyPostgresProfile extends ExtPostgresProfile
  with PgArraySupportFixed
  with PgDateSupportJoda
  with PgHStoreSupport
  with PgPlayJsonSupport
  with PgEnumSupportFixed
  with PgRangeSupport
  with PgSearchSupport
  with PgSyntaxSupport {

  override val pgjson = "jsonb"

  override val api: API = new API {}

  trait API extends super.API
    with FixedArrayImplicits with FixedArrayPlainImplicits
    with DateTimeImplicits with JodaDateTimePlainImplicits
    with HStoreImplicits with SimpleHStorePlainImplicits
    with JsonImplicits with PlayJsonPlainImplicits
    with RangeImplicits with SimpleRangePlainImplicits
    with SearchImplicits with SearchAssistants with SimpleSearchPlainImplicits
    with SyntaxImplicits with SyntaxAssistants
}

object MyPostgresProfile extends MyPostgresProfile
