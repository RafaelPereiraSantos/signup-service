package com.rafael.worker.kafka.pubsub

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.rafael.worker.kafka.config.KafkaConfig
import com.rafael.worker.common.models.EligibleCreatedEvent
import kafka.utils.ShutdownableThread
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.LongDeserializer
import org.apache.kafka.common.serialization.StringDeserializer
import java.time.Duration
import java.util.*


class EligibleConsumerKafka(
    private val objectMapper: ObjectMapper,
    private val kafkaConfig: KafkaConfig,
    private val topic: String = "eligibleCreated",
    private val groupId: String = "01",
    private val instanceId: String? = null,
    private val readCommitted: Boolean = true
): ShutdownableThread("example", false) {

    private val consumer: KafkaConsumer<Int, String> = createConsumer()

    override fun doWork() {
        consumer.subscribe(listOf(topic))
        val records = consumer.poll(Duration.ofSeconds(100))
        records.forEach { record ->
            println("--------")
            println("$groupId received message")
            println("from partition ${record.partition()}")
            println("(${record.key()}, ${record.value()})")
            println("at offset ${record.offset()}")

            val test = deserializeEligible(record.value())
            println(test)
        }
        consumer.commitAsync()
    }

    override fun name(): String? = null

    override fun isInterruptible(): Boolean = false

    private fun createConsumer(): KafkaConsumer<Int, String> {
        val props = Properties()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaConfig.bootstrapServerConfig()
        props[ConsumerConfig.GROUP_ID_CONFIG] = groupId
        instanceId?.let { id -> props[ConsumerConfig.GROUP_INSTANCE_ID_CONFIG] = id }
        props[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = "false"
        props[ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG] = "1000"
        props[ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG] = "30000"
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = LongDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        if (readCommitted) props[ConsumerConfig.ISOLATION_LEVEL_CONFIG] = "read_committed"
        props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"

        return KafkaConsumer(props)
    }

    private fun deserializeEligible(message: String): EligibleCreatedEvent? {
        try {
            return objectMapper.readValue<EligibleCreatedEvent>(message)
        } catch(e: Exception) {
            println(e)
        }
        return null
    }

}
