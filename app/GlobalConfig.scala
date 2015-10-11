package app
import akka.actor.{Props, ActorSystem}
import akka.routing.RoundRobinPool
import producers.MessageProducer

object GlobalConfig {
	val messageProducerSystem = ActorSystem("messageProducerSystem")
	val messageProducerRouter = messageProducerSystem.actorOf(
		Props[MessageProducer].withRouter(RoundRobinPool(5)), name = "messageProducerRouter")
}