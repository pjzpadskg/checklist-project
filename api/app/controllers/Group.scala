package controllers

import java.util.UUID
import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.i18n.I18nSupport
import scala.concurrent.{ Future, ExecutionContext }

import models.domain._
import models.domain.Group._
import models.domain.GroupCreateForm._
import models.domain.GroupDeleteForm._

import models.repo.GroupRepo

@Singleton
class GroupController @Inject()
(val controllerComponents: ControllerComponents, groupRepo: GroupRepo)
(implicit ec: ExecutionContext)
extends BaseController with I18nSupport {

  def create() = Action.async { implicit request =>
      groupCreateForm.bindFromRequest().fold(
        error => Future.successful(BadRequest(error.errorsAsJson)),
        data => {
          val id = UUID.randomUUID()
          val owner = data.owner match {
            case Some(x) => x
            case _ => request.session.get("email").get
          }
          groupRepo.create(Group(id, data.name, owner)).map {
            case true => Ok(Json.obj("id" -> id))
            case false => BadRequest
          }
        }
      ).recover{ case x: Exception => InternalServerError(x.getMessage) }
    }

  def get() = Action.async { implicit request =>
      groupRepo.get(request.session.get("email").get).map {
        data => Ok(Json.obj("groups" -> data))
      }.recover{ case x: Exception => InternalServerError(x.getMessage) }
    }

  def delete() = Action.async { implicit request =>
      groupDeleteForm.bindFromRequest().fold(
        error => Future.successful(BadRequest(error.errorsAsJson)),
        data => {
          groupRepo.delete(data.id).map {
            count => Ok
          }
        }
      ).recover{ case x: Exception => InternalServerError(x.getMessage) }
    }
}
