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

  // GET OWNED GROUPS OF ONWER

  def get(owner: String): Future[Seq[Group]] = db.run {
      groups.filter(g => g.owner === owner).result
    }

  // DELETE GROUP BY ID

  def delete(id: UUID): Future[Int] = db.run {
      groups.filter(g => g.id === id).delete
    }
}
