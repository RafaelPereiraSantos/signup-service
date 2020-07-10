package com.rafael.pubsub

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import com.rafael.models.EligibleCreatedEvent

import com.rafael.service.Association

class EligibleConsumer(val host: String = "localhost", val port: Int = 15672) {

    fun up() {
        val factory = ConnectionFactory()
        factory.host = host
        val connection = factory.newConnection()
        val channel = connection.createChannel()

        channel.queueDeclare(Recv.QUEUE_NAME, false, false, false, null)
        println("Consumer alive")

        val consumer = object : DefaultConsumer(channel) {
            override fun handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: ByteArray) {
                val message = String(body, charset("UTF-8"))
                println(" [x] Received '$message'")
                Association.associate(
                    parseMessage(message)
                )
            }
        }
        channel.basicConsume(Recv.QUEUE_NAME, true, consumer)
    }

    private fun parseMessage(message: String): EligibleCreatedEvent {
        val mapper = ObjectMapper()
        val node: JsonNode = mapper.readTree(message)
        return EligibleCreatedEvent(
            node["email_address"].asText(), node["employee_id"].asText(), node["company_id"].asInt()
        )
    }
}

class Recv {
    companion object {
        const val QUEUE_NAME = "eligibleCreated"
    }
}
