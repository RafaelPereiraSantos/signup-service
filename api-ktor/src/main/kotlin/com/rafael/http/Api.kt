package com.rafael.http

import com.rafael.config.ServerConfig
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

class Api(serverConfig: ServerConfig) {

    private  val server = embeddedServer(Netty, serverConfig.serverPort) {
        serviceConfiguration()
    }

    fun start(wait: Boolean = true) {
        server.start(wait = wait)
    }
}