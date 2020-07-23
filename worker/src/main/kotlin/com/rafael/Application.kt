package com.rafael

import com.rafael.config.RabbitConfig
import com.rafael.pubsub.EligibleConsumer
import io.github.cdimascio.dotenv.Dotenv

fun main() {
    val dotEnv = Dotenv.load()
    val default = RabbitConfig(
        dotEnv.getOrThrow("RABBITMQ_HOST"),
        dotEnv.getOrThrow("RABBITMQ_PORT").toInt()
    )

    EligibleConsumer(default).up()
}
