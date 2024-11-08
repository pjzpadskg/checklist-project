case class Invite(created: Long, receiver: String, idGroup: UUID)
object Invite {
  def unapply(i: Invite): Option[(Long, String, UUID)] =
    Some((i.created, i.receiver, i.idGroup))

  implicit val inviteForm: Form[Invite] = Form(
    mapping(
      "created" ->  longNumber,
      "receiver" -> nonEmptyText,
      "idGroup" -> uuid
    )(Invite.apply)(Invite.unapply)
  )
}

final class InviteTable(t: Tag) extends Table[Invite](t, "INVITES") {
  def created = column[Long]("CREATED")
  def receiver = column[String]("RECEIVER", O.Length(50))
  def idGroup = column[String]("ID_GROUP")
  def * = (created, receiver, idGroup).mapTo[Invite]
}

case class InviteWrite(
  created: Long,
  receiver: String,
  name: String,
  owner: String
)

object InviteWrite {
  implicit val inviteWrites: Writes[InviteWrite] = new Writes[InviteWrite] {
    def writes(i: InviteWrites) = Json.obj(
      "created" -> i.created,
      "receiver" -> i.receiver,
      "name" -> i.name,
      "owner" -> i.owner
    )
  }
}
