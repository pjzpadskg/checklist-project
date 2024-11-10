package models.domain

import java.util.UUID
import play.api.data._
import play.api.libs.json._
import play.api.data.Forms._
import slick.jdbc.PostgresProfile.api._

case class Invite(id: UUID, created: Long, receiver: String, idGroup: UUID)

case class InviteWrite(id: UUID, created: Long, receiver: String, idGroup: UUID, nameGroup: String, owner: String)
object InviteWrite {
  implicit val inviteWrite: Writes[InviteWrite] = new Writes[InviteWrite] {
    def writes(i: InviteWrite) = Json.obj(
      "id" -> i.id,
      "created" -> i.created,
      "receiver" -> i.receiver,
      "idGroup" -> i.idGroup,
      "nameGroup" -> i.nameGroup,
      "owner" -> i.owner
    )
  }
}

case class InviteForm(created: Long, receiver: String, idGroup: UUID)
object InviteForm {
  def unapply(i: InviteForm): Option[(Long, String, UUID)] =
    Some((i.created, i.receiver, i.idGroup))

  val inviteForm: Form[InviteForm] = Form(
    mapping(
      "created" ->  longNumber,
      "receiver" -> nonEmptyText,
      "idGroup" -> uuid
    )(InviteForm.apply)(InviteForm.unapply)
  )
}

final class InviteTable(t: Tag) extends Table[Invite](t, "INVITES") {
  def id = column[UUID]("ID", O.PrimaryKey)
  def created = column[Long]("CREATED")
  def receiver = column[String]("RECEIVER", O.Length(50))
  def idGroup = column[UUID]("ID_GROUP")
  def * = (id, created, receiver, idGroup).mapTo[Invite]
}

case class InviteDeleteForm(id: UUID)
object InviteDeleteForm {
  def unapply(i: InviteDeleteForm): Option[(UUID)] = Some((i.id))

  val inviteDeleteForm: Form[InviteDeleteForm] = Form(
    mapping(
      "idGroup" -> uuid
    )(InviteDeleteForm.apply)(InviteDeleteForm.unapply)
  )
}
