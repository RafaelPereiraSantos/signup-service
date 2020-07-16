package com.rafael.models

class SearchResult(private val endUsers: List<EndUser>) {
    fun uniqueResult() =
        if (endUsers.count() == 1) endUsers.first() else null
}