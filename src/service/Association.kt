package com.rafael.service

import com.rafael.models.EligibleCreatedEvent

object Association {
    fun associate(eligibleEvent: EligibleCreatedEvent) {
        println(eligibleEvent.emailAddress)
    }
}
