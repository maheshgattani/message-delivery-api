# message-delivery-api

REST API to post messages to a RabbitMQ exchange.

This application uses Akka to maintain a set number of RabbitMQ Producers and uses then in round robin fashion to post to RabbitMQ. A request is successful only after confirmation is received from RabbitMQ. As RabbitMQ documentation says, even in case of failures, message might get posted to the exchange.

If any of the works fail for any reason, they get reinitialized automatically.

For applications wanting to fire-and-forget a message to the exchange, fire-and-forget a message to this API servers the same purpose.

This application uses akka-rabbitmq library to communicate with RabbitMQ.

Every exchange being posted to should already exist. This is done with the intent to keep the performance optimanl by not checking the existance of the exchange.