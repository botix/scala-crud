  package repos

  import javax.inject.Inject
  import scala.concurrent.{ExecutionContext, Future}
  import play.modules.reactivemongo.ReactiveMongoApi
  import reactivemongo.play.json._
  import reactivemongo.play.json.collection.JSONCollection
  import reactivemongo.bson.{BSONDocument, BSONObjectID}
  import reactivemongo.api.{ReadPreference, Cursor}
  import reactivemongo.api.commands.WriteResult
  import play.api.libs.json.{ JsObject, Json }


  import models.Car

  class CarRepository @Inject()(
    implicit ec: ExecutionContext,
    reactiveMongoApi: ReactiveMongoApi
  ){

    private def collection: Future[JSONCollection] =
      reactiveMongoApi.database.map(_.collection("caradverts"))
    
    def list(sortby: String = "id", limit: Int = 100): Future[Seq[Car]] =
      collection.flatMap(_
        .find(BSONDocument())
        .sort(Json.obj(sortby -> 1))
        .cursor[Car](ReadPreference.primary)
        .collect[Seq](limit, Cursor.FailOnError[Seq[Car]]())
      )


    def listById(id: Int): Future[Option[Car]] = 
      collection.flatMap(
       _.find(BSONDocument("id" -> id))
        .one[Car]
      )


    def create(post: Car): Future[WriteResult] =
      collection.flatMap(_.insert(post))  


    def update(id: Int, post: Car): Future[Option[Car]] = 
      collection.flatMap(
        _.findAndUpdate(
          BSONDocument("id" -> id),
          BSONDocument(
            f"$$set" -> BSONDocument(
              "id"    -> post.id,
              "title" -> post.title,
              "fuelType" -> post.fuelType,
              "price" -> post.price,
              "isNew" -> post.isNew,
              "mileage" -> post.mileage,
              "firstRegistration" -> post.firstRegistration
            )
          ),true
        ).map(_.result[Car])
      )

    def destroy(id: Int): Future[Option[Car]] = 
      collection.flatMap(_.findAndRemove(BSONDocument("id" -> id)).map(_.result[Car]))
    
  }