package com.rafael.apiwebflux.http

import com.rafael.apiwebflux.service.EligibleSearchService
import com.rafael.models.Eligible
import com.rafael.service.EligibilitySearch
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait
import org.springframework.web.reactive.function.server.coRouter

fun eligibiltyRoute(eligibleSearchService: EligibleSearchService) = coRouter {
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
    filter { request, next ->
        if (request.queryParam("email").isPresent ||
            request.queryParam("company_member_token").isPresent ||
            request.queryParam("personal_document").isPresent
        ) {
            next(request)
        } else {
            status(HttpStatus.UNAUTHORIZED).buildAndAwait()
        }
    }
}


