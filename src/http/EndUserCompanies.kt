package com.rafael.http

import com.fasterxml.jackson.databind.SerializationFeature
import com.rafael.models.AppConfig
import com.rafael.models.EndUser
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine

class EndUserCompanies {

    val server = embeddedServer(Netty, AppConfig.serverConfig.port) {
        endUserCompaniesModule()
    }

    fun start(wait: Boolean = true): Unit {
        server.start(wait = wait)
    }
}

fun Application.endUserCompaniesModule() {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    routing {
        get("/health") {
            call.respond(mapOf("status" to "OK"))
        }

        get("/end-user-companies") {
            val eligible = EndUser(1, "teste@teste.com", "abc123", "2948")
            call.respond(eligible)
        }
    }
}
