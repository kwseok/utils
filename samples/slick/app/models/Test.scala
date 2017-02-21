package models

import play.api.libs.json.{Json, OFormat}

case class Test(a: String, b: String, c: String, d: String)

object Test {
  implicit val jsonFormat: OFormat[Test] = Json.format[Test]
}
