package models.repo

import java.util.UUID
import javax.inject._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.PostgresProfile
import scala.concurrent.{ Future, ExecutionContext }
import models.domain._

@Singleton
class InviteRepo @Inject()(dbcp: DatabaseConfigProvider)
(implicit ec: ExecutionContext) {
  val dbConfig = dbcp.get[PostgresProfile]
  import dbConfig._
  import profile.api._

  lazy val invites = TableQuery[InviteTable]
  lazy val groups = TableQuery[GroupTable]

  // INVITES A USER TO A GROUP

  def create(inv: Invite): Future[Int] = db.run { invites += inv }

  // GET INVITES OF A USER

  def get(receiver: String): Future[Seq[(UUID, Long, String, UUID, String, String)]] = 
    db.run {
      invites.filter(_.receiver === receiver)
            .join(groups)
            .on(_.idGroup === _.id)
            .map((i,g) => (i.id, i.created, i.receiver, g.id, g.name, g.owner))
            .result
    }

  // DELETE OR RESOLVE INVITE

  def delete(id: UUID): Future[Int] = db.run {
      invites.filter(_.id === id).delete
    }

}
