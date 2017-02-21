package models

import play.api.libs.json.{Json, OFormat}

case class Location(lat: Double, long: Double)

object Location {
  implicit val jsonFormat: OFormat[Location] = Json.format[Location]
}

case class Place(name: String, location: Location)

object Place {
  implicit val jsonFormat: OFormat[Place] = Json.format[Place]

  var list: List[Place] = List(
    Place("Sandleford", Location(51.377797, -1.318965)),
    Place("Watership Down", Location(51.235685, -1.309197))
  )

  def save(place: Place): Unit = {
    list = list ::: List(place)
  }
}
