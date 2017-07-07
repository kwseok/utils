package slick

import com.github.stonexx.slick.pg._
import com.github.tminglei.slickpg._

trait MyPostgresProfile extends ExtPostgresProfile
  with PgArraySupportFixed
  with PgDate2Support
  with PgHStoreSupport
  with PgPlayJsonSupport
  with PgEnumSupportFixed
  with PgRangeSupport
  with PgSearchSupport
  with PgStringSupport {

  override val pgjson = "jsonb"

  override val api: API = new API {}

  trait API extends super.API
    with FixedArrayImplicits with FixedArrayPlainImplicits
    with DateTimeImplicits with Date2DateTimePlainImplicits
    with HStoreImplicits with SimpleHStorePlainImplicits
    with JsonImplicits with PlayJsonPlainImplicits
    with RangeImplicits with SimpleRangePlainImplicits
    with SearchImplicits with SearchAssistants with SimpleSearchPlainImplicits
    with StringImplicits
}

object MyPostgresProfile extends MyPostgresProfile
