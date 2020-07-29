package com.rafael.worker.kafka.config

class KafkaConfig(val host: String, val port: Int) {
    fun bootstrapServerConfig(): String = host + ":" + port
}
