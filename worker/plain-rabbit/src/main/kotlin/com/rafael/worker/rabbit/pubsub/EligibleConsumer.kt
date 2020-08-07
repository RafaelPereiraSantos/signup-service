package com.rafael.worker.rabbit.pubsub

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.rabbitmq.client.*
import com.rafael.common.service.EndUserAssociation
import com.rafael.worker.common.models.EligibleCreatedEvent
import com.rafael.worker.rabbit.config.RabbitConfig

class EligibleConsumer(
    private val rabbitConfig: RabbitConfig,
    private val endUserAssociation: EndUserAssociation,
    private val objectMapper: ObjectMapper
) {

    private lateinit var channel: Channel

    private val errorQueue = Recv.QUEUE_NAME + "-error"

    fun up() {
        val factory = ConnectionFactory()

        factory.host = rabbitConfig.host
//        factory.port = AppConfig.rabbitConfig.port

        val connection = factory.newConnection()
        channel = connection.createChannel()

        channel.queueDeclare(Recv.QUEUE_NAME, false, false, false, null)
        channel.queueBind(
            Recv.QUEUE_NAME,
            Recv.EXCHANGE_NAME,
            Recv.BINDING_KEY
        )

        channel.queueDeclare(errorQueue, false, false, false, null)

        val consumer = object : DefaultConsumer(channel) {
            override fun handleDelivery(
                consumerTag: String,
                envelope: Envelope,
                properties: AMQP.BasicProperties,
                body: ByteArray
            ) {
                val message = String(body, charset("UTF-8"))
                println("Message received: '$message'")

                // TODO improve dead-letter/retry strategy
                try {
                    endUserAssociation.associate(
                        parseMessage(message).toEligible()
                    )
                } catch (e: Exception) {
                    println(e.message)
                    publishError(e.message + "->" + message)
                }
            }
        }

        channel.basicConsume(Recv.QUEUE_NAME, true, consumer)
        println("Consumer alive")
    }

    private fun parseMessage(message: String) = objectMapper.readValue<EligibleCreatedEvent>(message)

    private fun publishError(message: String) {
        channel.basicPublish(
            "",
            errorQueue,
            null,
            message.toByteArray(charset("UTF-8"))
        )
    }
}

class Recv {
    companion object {
        const val QUEUE_NAME = "eligibleCreated"
        const val BINDING_KEY = "eligible.created"
        const val EXCHANGE_NAME = "amq.topic"
    }
}
