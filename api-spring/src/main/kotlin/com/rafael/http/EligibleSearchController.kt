package com.rafael.http

import com.rafael.models.Eligible
import com.rafael.service.EligibilitySearch
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class EligibleSearchController(private val eligibilitySearch: EligibilitySearch) {

    @GetMapping("/eligibility")
    fun search(
        @RequestParam email: String?,
        @RequestParam token: String?,
        @RequestParam("personal_document") personalDocument: String?
    ): ResponseEntity<Eligible> {
        val searchResult = eligibilitySearch.searchBy(email, token, personalDocument)
        return searchResult.uniqueResult()
            ?.let { eligiblity -> ResponseEntity.ok(eligiblity) }
            ?: ResponseEntity.notFound().build()
    }
}
