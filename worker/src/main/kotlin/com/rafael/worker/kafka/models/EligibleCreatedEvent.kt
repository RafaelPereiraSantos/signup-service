package com.rafael.worker.kafka.models

import com.rafael.models.Eligible

class EligibleCreatedEvent(
    val emailAddress: String,
    val employeeId: String,
    val companyId: Int
) {
    fun toEligible() = Eligible(emailAddress, employeeId, "")
}
