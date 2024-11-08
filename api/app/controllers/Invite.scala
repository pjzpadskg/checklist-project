package controllers

import java.util.UUID
import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.i18n.I18nSupport
import scala.concurrent.{ Future, ExecutionContext }

import models.domain._
import models.domain.Invite._
import models.domain.InviteWrite._

import models.repo.InviteRepo

@Singleton
class MemberController @Inject()
(val controllerComponents: ControllerComponents, inviteRepo: InviteRepo)
(implicit ec: ExecutionContext)
extends BaseController with I18nSupport {

  def get(id: String) = Action.async { implicit request =>
      inviteRepo.get(UUID.fromString(id))
                .map(data => Ok(Json.obj("members" -> data)))
    }

  def create() = Action.async { implicit request =>
      inviteRepo.bindFromRequest().fold(
        error => Future.successful(BadRequest(error.errorsAsJson)),
        data => memberRepo.add(Member(data.idGroup, data.emailUser, false))
                          .map(count => Ok)
      ).recover{ case x: Exception => InternalServerError(x.getMessage) }
    }

  def delete() = Action.async { implicit request =>
      inviteRepo.bindFromRequest().fold(
        error => Future.successful(BadRequest(error.errorsAsJson)),
        data => memberRepo.delete(data).map(count => Ok)
      ).recover{ case x: Exception => InternalServerError(x.getMessage) }
    }

}
