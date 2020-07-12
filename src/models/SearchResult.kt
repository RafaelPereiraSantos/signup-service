package com.rafael.models

class SearchResult(val endUsers: List<EndUser>) {
    fun result(): EndUser? {
        return if (oneEndUserFound()) {
            endUsers.first()
        } else {
            null
        }
    }

    fun hasResult(): Boolean {
        return oneEndUserFound()
    }

    private fun oneEndUserFound(): Boolean {
        return endUsers.count() == 1
    }
}