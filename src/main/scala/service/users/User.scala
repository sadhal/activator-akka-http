package service.users

import java.util.Date

import spray.json.DefaultJsonProtocol._

final case class User(id: String, firstName: String, lastName: String, email: String, twitterHandle: String, createdOn: Date)

object User {
  import service.DateMarshalling._
  implicit val format = jsonFormat6(User.apply)
}
