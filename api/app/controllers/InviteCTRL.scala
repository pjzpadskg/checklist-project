package controllers

import java.util.UUID
import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.i18n.I18nSupport
import scala.concurrent.{ Future, ExecutionContext }

import models.domain._
import models.domain.InviteWrite._
import models.domain.InviteForm._
import models.domain.InviteDeleteForm._

import models.repo.InviteRepo

@Singleton
class InviteController @Inject()
(val controllerComponents: ControllerComponents, inviteRepo: InviteRepo)
(implicit ec: ExecutionContext)
extends BaseController with I18nSupport {
  
  def get() = Action.async { implicit request =>
      inviteRepo.get(request.session.get("email").get)
                .map(data => Ok(Json.toJson(data.map(
                              d => InviteWrite(d(0),d(1),d(2),d(3),d(4),d(5))))))
                .recover{ case x: Exception => InternalServerError(x.getMessage) }
    }

  def create() = Action.async { implicit request =>
      inviteForm.bindFromRequest().fold(
        error => Future.successful(BadRequest(error.errorsAsJson)),
        data => inviteRepo.create(
                Invite(UUID.randomUUID(), data.created, data.receiver, data.idGroup))
                          .map(count => Ok)
      ).recover{ case x: Exception => InternalServerError(x.getMessage) }
    }

  def delete() = Action.async { implicit request =>
      inviteDeleteForm.bindFromRequest().fold(
        error => Future.successful(BadRequest(error.errorsAsJson)),
        data => inviteRepo.delete(data.id).map(count => Ok)
      ).recover{ case x: Exception => InternalServerError(x.getMessage) }
    }

}
