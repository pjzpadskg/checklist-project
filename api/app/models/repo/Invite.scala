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

  // INVITES A USER TO A GROUP

  def add(member: Member): Future[Int] = db.run { members += member }

  // GET INVITES OF A USER

  def get(idGroup: UUID): Future[Seq[Member]] = db.run {
      members.filter(m => m.idGroup === idGroup).result
    }

  // DELETE OR RESOLVE INVITE

  def delete(member: MemberForm): Future[Int] = db.run {
      members.filter(m => m.idGroup === member.idGroup
                       && m.emailUser === member.emailUser).delete
    }

}
