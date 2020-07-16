package com.rafael.http

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.rafael.models.ExceptionMessage
import com.rafael.models.HttpException
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
            propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        }
    }

    install(StatusPages) {
        exception<MissingRequestParameterException> {
            val key = "request.missing.parameters"
            print("Logger: $key")
            call.respond(HttpStatusCode.BadRequest, key)
        }

        exception<HttpException> { e ->
            val exceptionMessage = e.exceptionMessage
            print("Logger: ${exceptionMessage.errorDescription}}")
            call.respond(HttpStatusCode.fromValue(e.statusCode), exceptionMessage)
        }

        exception<MissingKotlinParameterException> { e->
//          TODO the name of the param is being sent in camel case we msut send it snake case for both description and errorKey
            val message = "body must have ${e.parameter.name}"
            val exceptionMessage = ExceptionMessage(message, "request.missing.${e.parameter.name}")
            print("Logger: ${exceptionMessage.errorDescription}}")
            call.respond(HttpStatusCode.BadRequest, exceptionMessage)
        }

        exception<JsonParseException> { e ->
            val exceptionMessage = ExceptionMessage(e.message.orEmpty(), "request.invalid_body")
            call.respond(HttpStatusCode.BadRequest, exceptionMessage)
        }

        exception<Exception> { e ->
            val key = "request.ooopsy"
            print("Logger: $e")
            call.respond(HttpStatusCode.InternalServerError, key)
        }
        fun String.testGabryel(name: String) = 1
        "a".testGabryel("aa")
        val a: ((String) -> Int) = "String::"::testGabryel
    }


    routing {
        health()
        register()
        eligibility()
        associate()
    }
}
