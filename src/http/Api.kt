package com.rafael.http

import com.rafael.models.AppConfig
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

class Api {

    private  val server = embeddedServer(Netty, AppConfig.serverConfig.port) {
        serviceConfiguration()
    }

    fun start(wait: Boolean = true) {
        server.start(wait = wait)
    }
}