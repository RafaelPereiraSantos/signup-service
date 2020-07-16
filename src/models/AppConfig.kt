package com.rafael.models

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import java.lang.IllegalStateException

object AppConfig {
    private val env = dotenv()
    val serverConfig = ServerConfig(env.getOrThrow("PORT").toInt())
    val rabbitConfig = RabbitConfig(
        env.getOrThrow("RABBITMQ_HOST"),
        env.getOrThrow("RABBITMQ_PORT").toInt()
    )
}

class ServerConfig(val port: Int)

class RabbitConfig(val host: String, val port: Int)

fun Dotenv.getOrThrow(env: String) = this[env]
    ?: throw IllegalStateException("Can't seem to find '$env' environment variable")