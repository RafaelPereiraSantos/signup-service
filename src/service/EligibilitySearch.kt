package com.rafael.service

import com.rafael.models.Eligible
import com.rafael.models.SearchResult

class EligibilitySearch {
    fun searchBy(email: String?, token: String?, personal_document: String?): SearchResult {
        return SearchResult(
            listOf(Eligible(email ?: "", token ?: "", personal_document ?: ""))
        )
    }
}
