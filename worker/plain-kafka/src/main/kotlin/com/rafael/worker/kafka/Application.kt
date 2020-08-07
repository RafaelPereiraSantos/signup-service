package com.rafael.worker.kafka

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.rafael.common.getOrThrow
import com.rafael.worker.kafka.config.KafkaConfig
import com.rafael.worker.kafka.pubsub.EligibleConsumerKafka
import io.github.cdimascio.dotenv.Dotenv

fun main() {
    val dotEnv = Dotenv.load()

    val kafkaConfig = KafkaConfig(
            dotEnv.getOrThrow("KAFKA_HOST"),
            dotEnv.getOrThrow("KAFKA_PORT").toInt()
    )
    val objectMapper = jacksonObjectMapper().apply {
        propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }

    EligibleConsumerKafka(objectMapper, kafkaConfig).run()
}
