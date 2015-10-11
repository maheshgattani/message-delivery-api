package controllers

import akka.util.Timeout
import app.GlobalConfig
import play.api.mvc._
import producers.Message
import akka.pattern.ask
import scala.concurrent.duration._
import scala.concurrent.Await

class MessageProducerController extends Controller {

	def post = Action {
		implicit val timeout = Timeout(1 minutes)
		val future = GlobalConfig.messageProducerRouter ? Message("test", "test", "message-delivery-api")
		val didPublish: Boolean = Await.result(future.mapTo[Boolean], 1 minute)
		Ok(didPublish.toString)
	}
}
