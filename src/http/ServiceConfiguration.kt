package com.rafael.http

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.MissingRequestParameterException
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.routing
import java.lang.Exception
import javax.naming.AuthenticationException

fun Application.serviceConfiguration() {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    install(StatusPages) {
        exception<MissingRequestParameterException> {
            val key = "request.missing.parameters"
            print("Logger: $key")
            call.respond(HttpStatusCode.BadRequest, key)
        }

        exception<AuthenticationException> {
            val key = "request.user.unlogged"
            print("Logger: $key")
            call.respond(HttpStatusCode.Unauthorized, key)
        }

        exception<Exception> { e ->
            val key = "request.ooopsy"
            print("Logger: $e")
            call.respond(HttpStatusCode.InternalServerError, key)
        }
    }

    routing {
        health()
        register()
        eligibility()
        associate()
    }
}