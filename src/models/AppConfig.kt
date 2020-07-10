package com.rafael.models

import io.github.cdimascio.dotenv.dotenv

object AppConfig {
    private val env = dotenv()
    val serverConfig = ServerConfig(env["PORT"]!!.toInt())
    val rabbitConfig = RabbitConfig(env["RABBITMQ_HOST"]!!.toString())
}

class ServerConfig(val port: Int)

class RabbitConfig(val host: String)
