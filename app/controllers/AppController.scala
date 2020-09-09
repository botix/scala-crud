package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import scala.concurrent.ExecutionContext
import repos.CarRepository

@Singleton
class AppController @Inject()(
  implicit ec: ExecutionContext,
  val controllerComponents: ControllerComponents,
  postsRepo: CarRepository
) extends BaseController {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok("App works!")
  }

}
