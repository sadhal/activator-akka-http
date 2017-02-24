package service.users
import java.util

import com.mongodb.async.client.MongoClientSettings
import org.mongodb.scala.connection.ClusterSettings
import org.mongodb.scala.{Document, MongoClient, MongoCredential, MongoDatabase, Observer, ServerAddress}

import scala.concurrent.{Future, Promise}

/**
  * Created by sadmir on 2017-02-20.
  */
class UserRepositoryMongodb(val db: MongoDatabase) extends UserRepository {

  val collection = db.getCollection("personer")

  def docToUser(doc: Document): User = {
    val id = if (doc.getObjectId("_id") != null) doc.getObjectId("_id").toString else ""
    val firstName = doc.getString("firstName")
    val lastName = doc.getString("lastName")
    val email = doc.getString("email")
    val twitterHandle = doc.getString("twitterHandle")
    val createdOn = doc.getDate("createdOn")
    User(id, firstName, lastName, email, twitterHandle, createdOn)
  }

  override def getUsers: Future[List[User]] = {
    val p = Promise[List[User]]()

    collection.find().subscribe(new Observer[Document] {
      var ls = List[User]()
      override def onError(e: Throwable) = p.failure(e)

      override def onComplete() = p.success(ls)

      override def onNext(result: Document) = {
        ls = ls ++ List(docToUser(result))
      }
    })

    p.future
  }

  override def getUser(id: String): Future[Option[User]] = {
    val p = Promise[Option[User]]()

    import org.mongodb.scala.model.Filters._
    collection.find(equal("_id", id)) subscribe(new Observer[Document] {
      var user: Option[User] = None
      override def onError(e: Throwable) = p.failure(e)

      override def onComplete() = p.success(user)

      override def onNext(result: Document) = {
        if (id.equals(result.getObjectId("_id"))) {
          user = Some(docToUser(result))
        }
      }
    })

    p.future
  }

}

object UserRepositoryMongodb {

  def apply(): UserRepositoryMongodb = new UserRepositoryMongodb(mongo())

  private def mongo(): MongoDatabase = {
    val host = System.getenv("MONGODB_SERVICE_HOST")
    val port = System.getenv("MONGODB_SERVICE_PORT").toInt

    val dbname = orElse[String](System.getenv("MONGODB_DATABASE"), "sampledb")
    val username = orElse[String](System.getenv("MONGODB_USER"), "sadhal")
    val password = orElse[String](System.getenv("MONGODB_PASSWORD"), "sadhal").toCharArray

    val mc: MongoCredential = MongoCredential.createCredential(username, dbname, password)
    
    val hsts: util.List[ServerAddress] = new util.ArrayList[ServerAddress]()
    hsts.add(new ServerAddress(host, port))
    val clusterSettings: ClusterSettings = ClusterSettings.builder().hosts(hsts).build()

    val credentials: util.List[MongoCredential] = new util.ArrayList[MongoCredential]()
    credentials.add(mc)
    val settings: MongoClientSettings = MongoClientSettings.builder()
      .clusterSettings(clusterSettings)
      .credentialList(credentials).build()
    val mongoClient: MongoClient = MongoClient(settings)

    val db: MongoDatabase = mongoClient.getDatabase(dbname)
    db
  }

  def orElse[T](value: T, fallbackValue: T): T = if (value != null) value || fallbackValue
}