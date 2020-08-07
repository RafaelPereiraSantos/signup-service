package com.rafael.models

class SearchResult(val eligibles: List<Eligible> = emptyList()) {
    fun uniqueResult() = if (eligibles.count() == 1) eligibles.first() else null
}
