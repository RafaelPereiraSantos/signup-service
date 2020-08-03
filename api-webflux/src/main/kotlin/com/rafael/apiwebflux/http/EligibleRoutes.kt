package com.rafael.apiwebflux.http

import com.rafael.apiwebflux.service.EligibleSearchService
import com.rafael.models.Eligible
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.*
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.util.logging.Level
import java.util.logging.Logger

fun eligibiltyRoute(eligibleSearchService: EligibleSearchService) = myCoRouter {
    GET("/eligibility") { request ->
        println("yay! a request")

        val emailAddress = request.queryParam("email").orElse(null)
        val companyMemberToken = request.queryParam("company_member_token").orElse(null)
        val personalDocument = request.queryParam("personal_document").orElse(null)

        val result = eligibleSearchService
            .coSearchBy(emailAddress, companyMemberToken, personalDocument)
            .uniqueResult()
            ?: return@GET notFound().buildAndAwait()
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
        Logger.getGlobal().log(Level.WARNING, "${e.message} : ${getStackTrace(e)}")
        status(500).bodyValueAndAwait(
            Eligible(getStackTrace(e), "error key", "a", 1)
        )
    }
}

fun getStackTrace(e: Throwable): String {
    val sw: StringWriter = StringWriter()
    e.printStackTrace(PrintWriter(sw))
    return sw.toString()
}

