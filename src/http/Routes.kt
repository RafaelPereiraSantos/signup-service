package com.rafael.http

import com.rafael.models.EndUser
import com.rafael.models.RegisteringEndUser
import com.rafael.service.Association
import io.ktor.application.call
import io.ktor.features.MissingRequestParameterException
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import javax.naming.AuthenticationException

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