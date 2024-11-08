package models.repo

import java.util.UUID
import javax.inject._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.PostgresProfile
import scala.concurrent.{ Future, ExecutionContext }
import models.domain._

@Singleton
class MemberRepo @Inject()(dbcp: DatabaseConfigProvider)
(implicit ec: ExecutionContext) {
  val dbConfig = dbcp.get[PostgresProfile]
  import dbConfig._
  import profile.api._

  lazy val members = TableQuery[MemberTable]

  // ADD A MEMBER TO A GROUP

  def add(member: Member): Future[Int] = db.run { members += member }

  // GET MEMBERS OF A GROUP

  def get(idGroup: UUID): Future[Seq[Member]] = db.run {
      members.filter(m => m.idGroup === idGroup).result
    }

  // REMOVE MEMBER FROM GROUP

  def delete(member: MemberForm): Future[Int] = db.run {
      members.filter(m => m.idGroup === member.idGroup
                       && m.emailUser === member.emailUser).delete
    }

  // MARK MEMBER AS VIEWING IN THE GROUP
  //  - A user must only have one viewing for their joined groups
  // 1. Invert all current true to false
  // 2. Make member viewing to the given group
  def update(member: MemberForm): Future[Int] = db.run {
      (members.filter(m => m.emailUser === member.emailUser && m.viewing === true)
              .map(_.viewing).update((false))) >>
      (members.filter(m => m.emailUser === member.emailUser
                        && m.idGroup === member.idGroup)
              .map(_.viewing).update((true)))
    }
}
