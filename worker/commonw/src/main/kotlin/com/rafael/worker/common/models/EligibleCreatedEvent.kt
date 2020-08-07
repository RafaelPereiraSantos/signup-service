package com.rafael.worker.common.models

import com.rafael.common.models.Eligible

class EligibleCreatedEvent(
    val emailAddress: String,
    val employeeId: String,
    val companyId: Int
) {
    fun toEligible() = Eligible(emailAddress, employeeId, "", 0)
}
