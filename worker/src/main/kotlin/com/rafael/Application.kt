package com.rafael

import com.rafael.config.KafkaConfig
import com.rafael.config.RabbitConfig
import com.rafael.pubsub.EligibleConsumerKafka
import io.github.cdimascio.dotenv.Dotenv
import java.util.concurrent.CountDownLatch


fun main() {
    val dotEnv = Dotenv.load()
    val rabbitConfig = RabbitConfig(
        dotEnv.getOrThrow("RABBITMQ_HOST"),
        dotEnv.getOrThrow("RABBITMQ_PORT").toInt()
    )
    val kafkaConfig = KafkaConfig(
        dotEnv.getOrThrow("KAFKA_HOST"),
        dotEnv.getOrThrow("KAFKA_PORT").toInt()
    )
//    EligibleConsumer(default).up()
    EligibleConsumerKafka(kafkaConfig).run()
}
