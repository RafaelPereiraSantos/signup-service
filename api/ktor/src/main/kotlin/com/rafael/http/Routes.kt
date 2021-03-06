package com.rafael.http

import com.rafael.common.models.Eligible
import com.rafael.common.models.ExceptionMessage
import com.rafael.common.models.HttpException
import com.rafael.common.models.RegisteringEndUser
import com.rafael.common.service.EndUserAssociation
import com.rafael.common.service.EligibilitySearch
import io.ktor.application.call
import io.ktor.features.MissingRequestParameterException
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post

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

fun Route.associate(endUserAssociation: EndUserAssociation) {
    post("/associate") {
        val cookies = call.request.cookies
        // TODO implement an authorizer interceptor
        if (cookies["session"].isNullOrBlank()) {
            throw HttpException(401, ExceptionMessage("valid session's token not present", "client.unauthenticated"))
        }

        val endUser = Eligible("teste@teste.com", "abc123", "2948", 0)
        endUserAssociation.associate(endUser)
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
