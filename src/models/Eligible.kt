package com.rafael.models

data class Eligible(
    val email: String,
    val employee_id: String,
    val personal_document: String
) {
    constructor(event: EligibleCreatedEvent):
        this(event.emailAddress, event.employeeId, "")
}

// TODO improve the different types of EndUser available
data class RegisteringEndUser(val name: String, val emailAddress: String, val password: String)
