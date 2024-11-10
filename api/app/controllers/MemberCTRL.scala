package controllers

import java.util.UUID
import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.i18n.I18nSupport
import scala.concurrent.{ Future, ExecutionContext }

import models.domain._
import models.domain.Member._
import models.domain.MemberForm._

import models.repo.MemberRepo

@Singleton
class MemberController @Inject()
(val controllerComponents: ControllerComponents, memberRepo: MemberRepo)
(implicit ec: ExecutionContext)
extends BaseController with I18nSupport {

  def get(id: String) = Action.async { implicit request =>
      memberRepo.get(UUID.fromString(id))
                .map(data => Ok(Json.toJson(data)))
                .recover{ case x: Exception => InternalServerError(x.getMessage) }
    }

  def add() = Action.async { implicit request =>
      memberForm.bindFromRequest().fold(
        error => {
          Future.successful(BadRequest(error.errorsAsJson))
        },
        data => {
           val member = data.emailUser match {
              case Some(x) => x
              case _ => request.session.get("email").get
            }
            memberRepo.add(Member(data.idGroup, member, false)).map(count => Ok)
        }
      ).recover{ case x: Exception => InternalServerError(x.getMessage) }
    }

  def update() = Action.async { implicit request =>
      memberForm.bindFromRequest().fold(
        error => Future.successful(BadRequest(error.errorsAsJson)),
        data => memberRepo.update(data).map(count => Ok)
      ).recover{ case x: Exception => InternalServerError(x.getMessage) }
    }

  def delete() = Action.async { implicit request =>
      memberForm.bindFromRequest().fold(
        error => Future.successful(BadRequest(error.errorsAsJson)),
        data => memberRepo.delete(data).map(count => Ok)
      ).recover{ case x: Exception => InternalServerError(x.getMessage) }
    }

}
