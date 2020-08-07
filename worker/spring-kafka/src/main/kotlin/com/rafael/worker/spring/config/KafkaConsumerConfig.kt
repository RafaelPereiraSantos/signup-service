package com.rafael.worker.spring.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.rafael.worker.common.models.EligibleCreatedEvent
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.LongDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer

@EnableKafka
@Configuration
class KafkaConsumerConfig {

    @Bean
    fun consumerFactory(objectMapper: ObjectMapper): ConsumerFactory<Long, EligibleCreatedEvent> {
        val props = mapOf<String, Any>(
            ConsumerConfig.GROUP_ID_CONFIG to "groupId",
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092"
        )
        return DefaultKafkaConsumerFactory(
            props, LongDeserializer(), JsonDeserializer(EligibleCreatedEvent::class.java, objectMapper)
        )
    }

    @Bean
    fun kafkaListenerContainerFactory(
        consumerFactory: ConsumerFactory<Long, EligibleCreatedEvent>
    ): ConcurrentKafkaListenerContainerFactory<Long, EligibleCreatedEvent> {
        val factory = ConcurrentKafkaListenerContainerFactory<Long, EligibleCreatedEvent>()
        factory.consumerFactory = consumerFactory
        return factory
    }

    @Bean
    fun objectMapper(): ObjectMapper {
        return jacksonObjectMapper().apply {
            propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        }
    }
}
