package com.rafael.models

class SearchResult(private val endUsers: List<EndUser>) {
    fun uniqueResult() = endUsers.firstOrNull()
}