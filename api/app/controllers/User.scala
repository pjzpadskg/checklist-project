package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.i18n.I18nSupport
import scala.concurrent.{ Future, ExecutionContext }

import models.domain._
import models.domain.User._
import models.domain.LoginForm._

import models.repo.UserRepo

@Singleton
class UserController @Inject()
(val controllerComponents: ControllerComponents, userRepo: UserRepo) 
(implicit ec: ExecutionContext)
extends BaseController with I18nSupport {

  def register() = Action.async { implicit request => 
    registerForm.bindFromRequest().fold(
      error => Future.successful(BadRequest(error.errorsAsJson)),
      data => userRepo.create(data).map{
        case true => Ok
        case false => BadRequest
      }
    ).recover{ case x: Exception => InternalServerError(x.getMessage) }
  }

  def login() = Action.async { implicit request =>
    loginForm.bindFromRequest().fold(
      error => Future.successful(BadRequest(error.errorsAsJson)),
      data => userRepo.auth(data).map{
        case Some(name) => Ok(Json.obj("name" -> name))
                            .withSession("email" -> data.email)
        case None => Unauthorized
      }
    ).recover{ case x: Exception => InternalServerError(x.getMessage) }
  }

  def logout() = Action { implicit request: Request[AnyContent] => Ok.withNewSession }
}
