package com.rafael.http

import com.fasterxml.jackson.databind.SerializationFeature
import com.rafael.models.AppConfig
import com.rafael.models.EndUser
import com.rafael.models.RegisteringEndUser
import com.rafael.service.Association
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.basic
import io.ktor.client.request.request
import io.ktor.features.ContentNegotiation
import io.ktor.features.MissingRequestParameterException
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.caseInsensitiveMap
import io.ktor.util.getDigestFunction
import org.omg.PortableInterceptor.Interceptor
import java.lang.Exception
import java.lang.reflect.Parameter
import java.net.Authenticator
import javax.naming.AuthenticationException

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

    install(StatusPages) {
        exception<MissingRequestParameterException> {
            call.respond(HttpStatusCode.BadRequest, "Missing parameters")
        }

        exception<AuthenticationException> {
            call.respond(HttpStatusCode.Unauthorized, "Logged users only")
        }

        exception<Exception> {
            call.respond(HttpStatusCode.InternalServerError, "ooopsy")
        }
    }

    routing {
        health()
        register()
        eligibility()
        associate()
    }
}


fun Route.health() {
    get("/health") {
        call.respond(mapOf("status" to "OK"))
    }
}

fun Route.register(){
    post("/register") {
        // TODO receive json in snake_case
        val registeringEndUser = call.receive<RegisteringEndUser>()
        call.respond(registeringEndUser)
    }
}

fun Route.associate() {
    post("/associate") {
        val cookies = call.request.cookies
        // TODO implement an authorizer interceptor
        if (cookies["session"].isNullOrEmpty()) {
            throw AuthenticationException("Unlogged user")
        } else {
            val endUser = EndUser(1, "teste@teste.com", "abc123", "2948")
            Association.associate(endUser)
            call.respond(HttpStatusCode.Created)
        }
    }
}

fun Route.eligibility() {
    get("/eligibility") {

        // TODO implement an interceptor to check params rule
        fun missingParams(): Boolean {
            val params: Parameters = call.request.queryParameters
            return params["email"].isNullOrEmpty() ||
                    params["token"].isNullOrEmpty() ||
                    params["personal_document"].isNullOrEmpty()
        }

        if (missingParams()) {
            throw MissingRequestParameterException("Missing at least one parameter")
        } else {
            val endUser = EndUser(1, "teste@teste.com", "abc123", "2948")
            call.respond(endUser)
        }
    }
}
