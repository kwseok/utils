import javax.inject.{Inject, Singleton}

import filters.LoggingFilter
import play.api.http.HttpFilters

@Singleton
class Filters @Inject()(
  loggingFilter: LoggingFilter
) extends HttpFilters {

  override val filters = Seq(loggingFilter)
}
