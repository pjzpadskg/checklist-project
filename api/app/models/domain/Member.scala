package models.domain

import java.util.UUID
import play.api.data._
import play.api.libs.json._
import play.api.data.Forms._
import slick.jdbc.PostgresProfile.api._

case class Member(idGroup: UUID, emailUser: String, viewing: Boolean)
object Member {
  implicit val memberWrites: Writes[Member] = new Writes[Member] {
    def writes(g: Member) = Json.obj(
      "idGroup" -> g.idGroup,
      "emailUser" -> g.emailUser,
      "viewing" -> g.viewing
    )
  }
}

final class MemberTable(t: Tag) extends Table[Member](t, "MEMBERS") {
  def idGroup = column[UUID]("ID_GROUP")
  def emailUser = column[String]("EMAIL_USER", O.Length(50))
  def viewing = column[Boolean]("VIEWING")
  def * = (idGroup, emailUser, viewing).mapTo[Member]
}

case class MemberForm(idGroup: UUID, emailUser: String)
object MemberForm {
  def unapply(m: MemberForm): Option[(UUID, String)] = Some(m.idGroup, m.emailUser)
  val memberForm: Form[MemberForm] = Form(
    mapping(
      "idGroup" -> uuid,
      "emailUser" -> nonEmptyText
    )(MemberForm.apply)(MemberForm.unapply)
  )
}
