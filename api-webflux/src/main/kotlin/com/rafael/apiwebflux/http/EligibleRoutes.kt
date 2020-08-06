package com.rafael.apiwebflux.http

import com.rafael.apiwebflux.service.EligibleSearchService
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.*

fun reactorEligibiltyRoute(eligibleSearchService: EligibleSearchService) = router {
    GET("/reactor/eligibility") { request ->
        println("yay! a request")

        val emailAddress = request.queryParam("email").orElse(null)
        val companyMemberToken = request.queryParam("company_member_token").orElse(null)
        val personalDocument = request.queryParam("personal_document").orElse(null)

        val futureResult = eligibleSearchService
            .reactorSearchBy(emailAddress, companyMemberToken, personalDocument)

            futureResult.flatMap {
                val eligible = it.uniqueResult()
                when(eligible) {
                    null -> notFound().build()
                    else -> ok().bodyValue(eligible)
                }
            }
    }

    configureRejectionHandler()
}

fun coEligibiltyRoute(eligibleSearchService: EligibleSearchService) = coRouter {
    GET("/co/eligibility") { request ->
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

    configureRejectionHandler()
}

fun RouterFunctionDsl.configureRejectionHandler() {
    filter { request, next ->
        if (request.queryParam("email").isPresent ||
            request.queryParam("company_member_token").isPresent ||
            request.queryParam("personal_document").isPresent
        ) {
            next(request)
        } else {
            status(HttpStatus.BAD_REQUEST).build()
        }
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
