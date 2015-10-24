# Message Delivery API

REST API to post messages to a RabbitMQ exchange.

Faetures and requirements:

1. Producer Actor System.
   1. This application uses Akka to maintain a set number of RabbitMQ Producers and uses then in round robin fashion to post to RabbitMQ. A request is successful only after confirmation is received from RabbitMQ. As RabbitMQ documentation says, even in case of failures, message might get posted to the exchange.
2. Automatic reinitialization.
   1. If any of the workers fail for any reason, they get reinitialized automatically.
3. For applications wanting to fire-and-forget a message to the exchange, fire-and-forget a message to this API servers the same purpose.
4. This application uses akka-rabbitmq library to communicate with RabbitMQ.
5. Every exchange being posted to should already exist. This is done with the intent to keep the performance optimal by not checking the existance of the exchange.
