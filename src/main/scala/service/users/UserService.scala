package service.users

import javax.inject.{Inject, Named}

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import spray.json.DefaultJsonProtocol

@Named
class UserService @Inject()(userRepository: UserRepository) extends DefaultJsonProtocol with SprayJsonSupport {

  val userRoutes =
    path("personer") {
      get {
        complete(userRepository.getUsers)
      }
    } ~
      path("personer" / Segment) { id =>
        complete(userRepository.getUser(id))
      }
}
