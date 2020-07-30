package com.rafael.worker.spring.pubsub

import com.rafael.worker.kafka.models.EligibleCreatedEvent
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class EligibleConsumer {

    @KafkaListener(topics = ["eligibleCreated"], groupId = "eligibleCreatedConsumer")
    fun listenEligibleCreated(message: EligibleCreatedEvent) {
        println("Received Messasge in group foo: $message")
    }
}
