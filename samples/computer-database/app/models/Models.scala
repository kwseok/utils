package models

import java.util.Date

import com.github.stonexx.play.mvc.binders.enum._
import com.github.stonexx.scala.data.SortableEnumeration

case class Company(id: Option[Long], name: String)

case class Computer(
  id: Option[Long] = None,
  name: String,
  introduced: Option[Date] = None,
  discontinued: Option[Date] = None,
  companyId: Option[Long] = None
)

object Computer {
  object forms {
    object Sorts extends SortableEnumeration {
      type Sorts = Value
      val Id           = Value
      val Name         = Value
      val Introduced   = Value
      val Discontinued = Value
      val Company      = Value

      implicit val pathBindable        = enumSortPathBindable(this)
      implicit val queryStringBindable = enumSortQueryStringBindable(this)
    }
  }
}
