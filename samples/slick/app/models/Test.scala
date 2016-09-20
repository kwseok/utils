package models

import play.api.libs.json.Json

case class Test(a: String, b: String, c: String, d: String)

object Test {
  implicit val jsonFormat = Json.format[Test]
}
