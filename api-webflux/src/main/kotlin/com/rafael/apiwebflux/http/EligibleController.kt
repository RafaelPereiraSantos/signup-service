package com.rafael.apiwebflux.http

import com.rafael.service.EligibilitySearch
import kotlinx.coroutines.flow.flow
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.coRouter


val eligibilitySearch = EligibilitySearch()

val route = coRouter {
    GET("/eligibility") {
        return@GET ok().bodyAndAwait(flow {
            emit(eligibilitySearch.searchBy("a","a","a").uniqueResult()?: "")
        })
    }
}


