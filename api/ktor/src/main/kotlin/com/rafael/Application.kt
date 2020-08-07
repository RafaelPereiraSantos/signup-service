package com.rafael

import com.rafael.common.getOrThrow
import com.rafael.config.ServerConfig
import com.rafael.http.Api
import io.github.cdimascio.dotenv.Dotenv

fun main() {
    val dotEnv = Dotenv.load()
    val serverConfig = ServerConfig(
        dotEnv.getOrThrow("PORT").toInt(),
        dotEnv.getOrThrow("CORE_BASE_URL")
    )

    Api(serverConfig).start()
}