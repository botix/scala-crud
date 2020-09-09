package controllers

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import play.api._
import play.api.mvc._
import play.api.libs.json._
import repos.CarRepository
import reactivemongo.bson.BSONObjectID
import models.Car


class CarController @Inject() (
  implicit ec: ExecutionContext,
  components: ControllerComponents,
  carsRepo: CarRepository

) extends AbstractController(components) {

  def listCarAdvert(sortby: String = "id") = Action.async {
    carsRepo.list(sortby).map{cars => 
      Ok(Json.toJson(cars))
    }
  }

  def listCarAdvertById(id: Int) = Action.async {
    carsRepo.listById(id).map { maybeCar => 
      maybeCar.map { car => 
        Ok(Json.toJson(car))
      }.getOrElse(NotFound("No car was found under specified id"))
    }
  }
  
  def createCarAdvert = Action.async(parse.json) {
    _.body
      .validate[Car]
      .map { car =>
        carsRepo.create(car).map { _ => 
          Created
        }
      }.getOrElse(Future.successful(BadRequest("Invalid format")))  
  }

  def updateCarAdvert(id: Int) = Action.async(parse.json)
  {
    _.body.validate[Car].map{ car =>
      carsRepo.update(id, car)map {
        case Some(car)  => Ok(Json.toJson(car))
        case _          => NotFound("No car was found under specified id")
      }
    }.getOrElse(Future.successful(BadRequest("Invalid Format")))
  }

  def deleteCarAdvert(id: Int) = Action.async {
    carsRepo.destroy(id).map {
      case Some(car) => Ok(Json.toJson(car))
      case _         => NotFound("No car was found under specified id")
    }
  }

}