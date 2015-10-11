package producers

import akka.actor.Actor
import com.rabbitmq.client.{ShutdownSignalException, ConfirmListener}
import com.thenewmotion.akka.rabbitmq._
import play.Play

case class Message(message: String, routingKey: String, exchange: String)
case class NackException(message: String) extends Exception

class MessageProducer extends Actor {

	val config = Play.application.configuration.getConfig("rabbitmq")
	val factory = new ConnectionFactory()
	factory.setHost(config.getString("host"))
	factory.setPort(config.getString("port").toInt)
	factory.setUsername(config.getString("user"))
	factory.setPassword(config.getString("password"))

	var channel = setup

	def setup = {
		val connection: Connection = factory.newConnection()
		val channel = connection.createChannel()
		channel.confirmSelect()
		channel.addConfirmListener(new MessageProducerConfirmListener)
		channel
	}

	def post(postStr: String, routingKey: String, exchange: String) : Boolean = {
		try {
			channel.basicPublish(exchange, routingKey, null, postStr.getBytes("UTF-8"))
			channel.waitForConfirmsOrDie(10)
			true
		} catch {
			case e: ShutdownSignalException => {
				channel = setup
				false
			}
			case e: NackException => {
				channel = setup
				false
			}
			case e: Exception => {
				false
			}
		}
	}

	def receive = {
		case Message(message, routingKey, exchange) => {
			sender ! post(message, routingKey, exchange)
		}
	}
}

class MessageProducerConfirmListener extends ConfirmListener {
	def handleAck(deliveryTag: Long, multiple: Boolean): Unit = {}

	def handleNack(deliveryTag: Long, multiple: Boolean): Unit = {
		throw new NackException("Nack Received")
	}
}