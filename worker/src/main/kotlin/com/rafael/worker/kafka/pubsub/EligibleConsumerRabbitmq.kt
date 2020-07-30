package com.rafael.worker.kafka.pubsub

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.*
import com.rafael.worker.kafka.config.RabbitConfig
import com.rafael.worker.kafka.models.EligibleCreatedEvent
import com.rafael.service.EndUserAssociation

class EligibleConsumer(val rabbitConfig: RabbitConfig) {

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
                    EndUserAssociation.associate(
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

    private fun parseMessage(message: String): EligibleCreatedEvent {
        val mapper = ObjectMapper()
        val node: JsonNode = mapper.readTree(message)
        return EligibleCreatedEvent(
            node["email_address"].asText(), node["employee_id"].asText(), node["company_id"].asInt()
        )
    }

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