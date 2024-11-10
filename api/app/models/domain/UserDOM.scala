package models.domain

import play.api.data._
import play.api.data.Forms._
import slick.jdbc.PostgresProfile.api._

case class User (email: String, password: String, name: String)
object User {
	def unapply(u: User): Option[(String, String, String)] =
		Some((u.email, u.password, u.name))

	val registerForm: Form[User] = Form(
		mapping(
			"email" -> nonEmptyText,
			"password" -> nonEmptyText,
			"name" -> nonEmptyText
		)(User.apply)(User.unapply)
	)
}

case class LoginForm (email: String, password: String)
object LoginForm {
	def unapply(l: LoginForm): Option[(String, String)] =
		Some((l.email, l.password))

	val loginForm: Form[LoginForm] = Form(
		mapping(
			"email" -> nonEmptyText,
			"password" -> nonEmptyText
		)(LoginForm.apply)(LoginForm.unapply)
	)
}

final class UserTable(t: Tag) extends Table[User](t, "USERS") {
	def email = column[String]("EMAIL", O.PrimaryKey, O.Length(255))
	def password = column[String]("PASSWORD", O.Length(255))
	def name = column[String]("NAME", O.Length(255))
	def * = (email, password, name).mapTo[User]
}