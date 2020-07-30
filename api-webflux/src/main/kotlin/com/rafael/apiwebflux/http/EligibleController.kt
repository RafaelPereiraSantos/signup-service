package com.rafael.apiwebflux.http

import com.rafael.service.EligibilitySearch
import org.springframework.web.reactive.function.server.router

fun eligibiltyRoute(eligibilitySearch: EligibilitySearch) = router {
    GET("/eligibility") {
        val result = eligibilitySearch
            .searchBy("a","a","a").uniqueResult()
            ?: return@GET notFound().build()

        ok().bodyValue(result)
    }
}


