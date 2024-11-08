package models.domain

import java.util.UUID
import play.api.data._
import play.api.libs.json._
import play.api.data.Forms._
import slick.jdbc.PostgresProfile.api._

case class Group (id: UUID, name: String, owner: String)
object Group {
  implicit val groupWrites: Writes[Group] = new Writes[Group] {
    def writes(g: Group) = Json.obj(
      "id" -> g.id,
      "name" -> g.name,
      "owner" -> g.owner
    )
  }
}

final class GroupTable(t: Tag) extends Table[Group](t, "GROUPS") {
  def id = column[UUID]("ID", O.PrimaryKey)
  def name = column[String]("NAME", O.Length(255))
  def owner = column[String]("OWNER", O.Length(50))
  def * = (id, name, owner).mapTo[Group]
}

case class GroupCreateForm (name: String, owner: Option[String])
object GroupCreateForm {
  def unapply(g: GroupCreateForm): Option[(String, Option[String])] = Some((g.name, g.owner))

  val groupCreateForm: Form[GroupCreateForm] = Form(
    mapping(
      "name" -> nonEmptyText,
      "owner" -> optional(text)
    )(GroupCreateForm.apply)(GroupCreateForm.unapply)
  )
}

case class GroupDeleteForm (id: UUID)
object GroupDeleteForm {
  def unapply(g: GroupDeleteForm): Option[(UUID)] = Some((g.id))

  val groupDeleteForm: Form[GroupDeleteForm] = Form(
    mapping("id" -> uuid)(GroupDeleteForm.apply)(GroupDeleteForm.unapply)
  )
}

