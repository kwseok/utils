package models

import com.github.stonexx.play.json.enum._
import play.api.libs.json.{Format, Json, OFormat}

case class Cat(
  name: String,
  color: String,
  flag: Boolean,
  state: Cat.State.Value
)

object Cat {
  object State extends Enumeration {
    type State = Option
    val Bad      = Option("The bad")
    val Normal   = Option("The normal")
    val Good     = Option("The good")
    val VeryGood = Option("The very good")

    protected case class Option(label: String) extends super.Val()

    implicit def toOption(value: Value): Option = value.asInstanceOf[Option]

    implicit val jsonFormat: Format[Value] = enumFormat(this)
  }

  implicit val jsonFormat: OFormat[Cat] = Json.format[Cat]

  object forms {
    import com.github.stonexx.play.data.forms.enum._
    import play.api.data._
    import play.api.data.Forms._

    final val catForm = Form(
      mapping(
        "name" -> text,
        "color" -> text,
        "flag" -> ignored(true),
        "state" -> enum(Cat.State)
      )(Cat.apply)(Cat.unapply)
    )
  }
}
