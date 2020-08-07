package com.rafael.apiwebflux.http

import com.rafael.apiwebflux.service.EligibleSearchService
import com.rafael.common.models.Eligible
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class EligibleController(private val eligibleSearchService: EligibleSearchService) {

    @GetMapping("/web-flux-controller/eligibility")
    fun search(
        @RequestHeader("x-session") session: String,
        @RequestParam email: String?,
        @RequestParam token: String?,
        @RequestParam("personal_document") personalDocument: String?
    ): ResponseEntity<Eligible> {
        println(session)
        val result = eligibleSearchService
            .searchBy(email, token, personalDocument)

        return result.uniqueResult()
            ?.let { eligibility -> ResponseEntity.ok(eligibility) }
            ?: ResponseEntity.notFound().build()
    }
}
