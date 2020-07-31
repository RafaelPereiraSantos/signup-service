package com.rafael.apiwebflux.http

import com.rafael.apiwebflux.service.EligibleSearchService
import com.rafael.models.Eligible
import com.rafael.service.EligibilitySearch
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.reactive.function.server.*
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger

fun eligibiltyRoute(eligibleSearchService: EligibleSearchService) = myCoRouter {
    GET("/eligibility") { request ->
        println("yay! a request")

        val emailAddress = request.queryParam("email").orElse("")
        val companyMemberToken = request.queryParam("company_member_token").orElse("")
        val personalDocument = request.queryParam("personal_document").orElse("")

        val result = eligibleSearchService
            .searchBy(emailAddress, companyMemberToken, personalDocument)
            ?: return@GET ok().bodyValueAndAwait(emptyList<Eligible>())
        return@GET ok().bodyValueAndAwait(result)
    }
}

fun myCoRouter(routes: (CoRouterFunctionDsl.() -> Unit)): RouterFunction<ServerResponse> {
    return coRouter {
        routes()
        configureRejectionHandler()
        configureErrorHandler()
    }
}

fun CoRouterFunctionDsl.configureRejectionHandler() {
    filter { request, next ->
        if (request.queryParam("email").isPresent ||
            request.queryParam("company_member_token").isPresent ||
            request.queryParam("personal_document").isPresent
        ) {
            next(request)
        } else {
            status(HttpStatus.BAD_REQUEST).buildAndAwait()
        }
    }
}

fun CoRouterFunctionDsl.configureErrorHandler() {
    onError<IOException> { e, _ ->
        Logger.getGlobal().log(Level.SEVERE, "${e.message} : Can't connect to the server")
        status(500).buildAndAwait()
    }
    onError<IllegalStateException> {  e, _ ->
        Logger.getGlobal().log(Level.WARNING, "${e.message} : Can't connect to the server")
        status(500).buildAndAwait()
    }
    onError<Exception> { e, _ ->
        Logger.getGlobal().log(Level.WARNING, "${e.message} : something wrong happend")
        status(500).bodyValueAndAwait(
            Eligible(e.message.toString(), "error key", "a", 1)
        )
    }
}

