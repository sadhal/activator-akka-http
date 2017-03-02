package service.users

import java.util.Date

import scala.concurrent.Future

trait UserRepository {
  def getUsers: Future[Seq  [User]]
  def getUser(id: String): Future[Option[User]]
}

final class UserRepositoryImpl extends UserRepository {

  private val users = List(
    User("0", "Jack", "Dorsey", "jack@twitter.com", Option("jack"), new Date()),
    User("1", "Steve", "Jobs", "steve@apple.com", Option("jobs"), new Date())
  )

  def getUsers: Future[Seq[User]] =
    Future.successful(users)

  def getUser(id: String): Future[Option[User]] = {
    users.filter(u => u.id == id) match {
      case Nil => Future.successful(None)
      case x :: Nil => Future.successful(Some(x))
      case _ => Future.failed(new IllegalStateException("multiple users with same id"))
    }
  }
}
