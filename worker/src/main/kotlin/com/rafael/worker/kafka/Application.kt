package com.rafael.worker.kafka

import com.rafael.getOrThrow
import com.rafael.worker.kafka.config.KafkaConfig
import com.rafael.worker.kafka.config.RabbitConfig
import com.rafael.worker.kafka.pubsub.EligibleConsumerKafka
import io.github.cdimascio.dotenv.Dotenv


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
