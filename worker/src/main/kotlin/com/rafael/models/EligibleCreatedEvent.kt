package com.rafael.models

class EligibleCreatedEvent(
    val emailAddress: String,
    val employeeId: String,
    val companyId: Int
) {
    fun toEligible() = Eligible(emailAddress, employeeId, "")
}
