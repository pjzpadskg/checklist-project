package models.repo

import java.util.UUID
import javax.inject._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.PostgresProfile
import scala.concurrent.{ Future, ExecutionContext }
import models.domain._

@Singleton
class GroupRepo @Inject()(dbcp: DatabaseConfigProvider)
(implicit ec: ExecutionContext) {
  val dbConfig = dbcp.get[PostgresProfile]
  import dbConfig._
  import profile.api._

  lazy val groups = TableQuery[GroupTable]
  lazy val members = TableQuery[MemberTable]
  lazy val users = TableQuery[UserTable]
  // CREATE A GROUP
  // - check if group name already exists in user context
  //   - if it exists, return false
  //   - otherwise, create group and return true

  def create(group: Group): Future[Boolean] = db.run {
      groups.filter(g => g.owner === group.owner && g.name === group.name)
            .result.headOption
    }.map {
      case Some(_) => false
      case _ => {
        db.run( groups += group )
        true
      }
    }

  // GET ALL GROUPS ASSOCIATED BY USER

  def get(user: String): Future[Seq[(UUID, String, String, String)]] = db.run {
      members.filter(_.emailUser === user)
        .join(groups).on(_.idGroup === _.id)
        .join(users).on{ case ((m,g),u) => g.owner === u.email }
        .map{ case ((m,g),u) => (g.id, g.name, g.owner, u.name) }
        .result
    }

  // DELETE GROUP BY ID

  def delete(id: UUID): Future[Int] = db.run {
      groups.filter(g => g.id === id).delete
    }
}
