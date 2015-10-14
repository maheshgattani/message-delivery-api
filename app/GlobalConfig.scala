package app
import akka.actor.{Props, ActorSystem}
import akka.routing.RoundRobinPool
import play.Play
import producers.MessageProducer

object GlobalConfig {
	val config = Play.application.configuration.getConfig("rabbitmq")
	val messageProducerSystem = ActorSystem("messageProducerSystem")
	val numberOfProducers = config.getString("number-of-producers").toInt
	val messageProducerRouter = messageProducerSystem.actorOf(
		Props[MessageProducer].withRouter(RoundRobinPool(numberOfProducers)), name = "messageProducerRouter")
}