package models.repo

import java.util.UUID
import java.time.LocalDateTime
import javax.inject._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.PostgresProfile
import scala.concurrent.{ Future, ExecutionContext }
import models.domain._

@Singleton
class UserRepo @Inject()(dbcp: DatabaseConfigProvider)
(implicit ec: ExecutionContext) {
	val dbConfig = dbcp.get[PostgresProfile]
	import dbConfig._
	import profile.api._

	lazy val users = TableQuery[UserTable]

	// REGISTER A USER
	// - check if the user already exists
	// 	 - if it exists, return false
	//   - otherwise, create the user and return true

	def create(user: User): Future[Boolean] = db.run {
			users.filter(_.email === user.email).result.headOption
		}.map {
			case Some(_) => false 
			case _ => {
				db.run( users += user )
				true
			}
		}

	// AUTHENTICATE A USER
	// - check if user credentials exist
	//		- if yes, return name of user. otherwise, return none.

	def auth(user: LoginForm): Future[Option[String]] = db.run {
			users.filter(u => 
							u.email === user.email && 
							u.password === user.password)
				.result.headOption
		}.map {
			case Some(x) => Some(x.name)
			case _ => None
		}

}