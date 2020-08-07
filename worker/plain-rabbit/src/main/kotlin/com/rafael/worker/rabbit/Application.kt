package com.rafael.worker.rabbit

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.rafael.common.getOrThrow
import com.rafael.common.service.EndUserAssociation
import com.rafael.worker.rabbit.config.RabbitConfig
import com.rafael.worker.rabbit.pubsub.EligibleConsumer
import io.github.cdimascio.dotenv.Dotenv

fun main() {
    val dotEnv = Dotenv.load()
    val rabbitConfig = RabbitConfig(
            dotEnv.getOrThrow("RABBITMQ_HOST"),
            dotEnv.getOrThrow("RABBITMQ_PORT").toInt()
    )

    val objectMapper = jacksonObjectMapper().apply {
        propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }

    EligibleConsumer(rabbitConfig, EndUserAssociation(), objectMapper).up()
}
