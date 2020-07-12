package com.rafael.service

import com.rafael.models.EndUser
import com.rafael.models.SearchResult

class EligibilitySearch() {
    fun searchBy(email: String?, token: String?, personal_document: String?): SearchResult {
        return SearchResult(
            listOf(EndUser(1, email ?: "", token ?: "", personal_document ?: ""))
        )
    }
}