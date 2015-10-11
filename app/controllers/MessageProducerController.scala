package controllers

import akka.util.Timeout
import app.GlobalConfig
import play.api.mvc._
import producers.Message
import akka.pattern.ask
import scala.concurrent.duration._
import scala.concurrent.Await
import play.api.data._
import play.api.data.Forms._

class MessageProducerController extends Controller {

	implicit val timeout = Timeout(1 minutes)
	val postForm = Form(
		mapping(
			"data" -> text,
			"routing_key" -> text,
			"exchange" -> text
		)(Message.apply)(Message.unapply)
	)

	def post = Action { implicit request =>
		val message = postForm.bindFromRequest().get
		val future = GlobalConfig.messageProducerRouter ? message
		val didPublish: Boolean = Await.result(future.mapTo[Boolean], 1 minute)
		if (didPublish)
			Ok
		else
			ServiceUnavailable
	}
}
