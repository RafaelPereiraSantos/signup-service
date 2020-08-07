package com.rafael.common.service

import com.rafael.common.models.Eligible
import com.rafael.common.models.SearchResult

class EligibilitySearch {
    fun searchBy(email: String?, token: String?, personal_document: String?): SearchResult {
        return SearchResult(
            listOf(Eligible(email ?: "", token ?: "", personal_document ?: "", 1))
        )
    }
}
