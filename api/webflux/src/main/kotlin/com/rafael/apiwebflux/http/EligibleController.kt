package com.rafael.apiwebflux.http

import com.rafael.apiwebflux.service.EligibleSearchService
import com.rafael.common.models.Eligible
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class EligibleController(private val eligibleSearchService: EligibleSearchService) {

    @GetMapping("/web-flux-controller/eligibility")
    fun search(
        @RequestParam email: String?,
        @RequestParam token: String?,
        @RequestParam("personal_document") personalDocument: String?
    ): ResponseEntity<Eligible> {
        val result = eligibleSearchService
            .searchBy(email, token, personalDocument)

        return result.uniqueResult()
            ?.let { eligibility -> ResponseEntity.ok(eligibility) }
            ?: ResponseEntity.notFound().build()
    }
}
