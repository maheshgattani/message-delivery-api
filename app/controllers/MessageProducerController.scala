package controllers

import akka.util.Timeout
import app.GlobalConfig
import play.api.libs.json.Json
import play.api.mvc._
import producers.Message
import akka.pattern.ask
import scala.concurrent.duration._
import scala.concurrent.Await
import play.api.data._
import play.api.data.Forms._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._

class MessageProducerController extends Controller {

	implicit val timeout = Timeout(1 minutes)
	val postForm = Form(
		mapping(
			"data" -> nonEmptyText,
			"routing_key" -> nonEmptyText,
			"exchange" -> nonEmptyText
		)(Message.apply)(Message.unapply)
	)

	def post = Action { implicit request =>
		postForm.bindFromRequest().fold (
			formWithErrors => {
				BadRequest(Json.stringify(formWithErrors.errorsAsJson))
			},
			message => {
				val future = GlobalConfig.messageProducerRouter ? message
				val didPublish: Boolean = Await.result(future.mapTo[Boolean], 1 minute)
				if (didPublish)
					Ok
				else
					ServiceUnavailable
			}
		)
	}
}
