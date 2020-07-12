package com.rafael.models

data class EndUser(
    val person_id: Int,
    val email: String,
    val employee_id: String,
    val personal_document: String
) {
    constructor(event: EligibleCreatedEvent):
            this(0, event.emailAddress, event.employeeId, "")
}

// TODO improve the different types of EndUser available
data class RegisteringEndUser(val name: String, val emailAddress: String, val password: String)
