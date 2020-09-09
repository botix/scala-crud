package models

import play.api.libs.json.{Json, OFormat}
import reactivemongo.bson.BSONObjectID
import reactivemongo.play.json._

case class Car(
  _id: Option[BSONObjectID],
  id: Int,                              //how to make this unique?
  title: String,
  fuelType: String,
  price: Int,                           //how to make a union type? [Float | Int]
  isNew: Boolean,
  mileage: Option[Int],
  firstRegistration: Option[String]     //how to handle dates in Scala?
)

object Car {
  implicit val format = Json.format[Car]
}
