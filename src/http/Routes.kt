package com.rafael.http

import com.rafael.models.EndUser
import com.rafael.models.RegisteringEndUser
import com.rafael.service.EndUserAssociation
import com.rafael.service.EligibilitySearch
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.features.MissingRequestParameterException
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.util.pipeline.PipelineContext
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
        call.respond(HttpStatusCode.Created, registeringEndUser)
    }
}

fun Route.associate() {
    post("/associate") {
        val cookies = call.request.cookies
        // TODO implement an authorizer interceptor
        if (cookies["session"].isNullOrEmpty()) {
            throw AuthenticationException("Unlogged user")
        }

        val endUser = EndUser(1, "teste@teste.com", "abc123", "2948")
        EndUserAssociation.associate(endUser)
        call.respond(HttpStatusCode.Created)
    }
}

fun Route.eligibility() {
    get("/eligibility") {
        // TODO implement an interceptor to check params rule
        val params = call.request.queryParameters

        if (params.missingParams())
            throw MissingRequestParameterException("Missing at least one parameter")

        val result = EligibilitySearch().searchBy(
            params["email"], params["token"], params["personal_document"]
        )
        call.respond(result.uniqueResult() ?: HttpStatusCode.NotFound)
    }
}

fun Parameters.missingParams(): Boolean {
    return this["email"].isNullOrEmpty() &&
            this["token"].isNullOrEmpty() &&
            this["personal_document"].isNullOrEmpty()
}