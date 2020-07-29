package com.rafael.worker.spring.pubsub

import com.rafael.worker.kafka.models.EligibleCreatedEvent
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class EligibleConsumer {

    @KafkaListener(topics = ["eligibleCreated"], groupId = "eligibleCreatedConsumer")
    fun listenEligibleCreated(@Payload message: EligibleCreatedEvent) {
        println("Received Messasge in group foo: $message")
    }
}
