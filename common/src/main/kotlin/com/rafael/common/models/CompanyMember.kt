package com.rafael.common.models

data class CompanyMember(
    val emailAddress: String,
    val token: String,
    val cpf: String,
    val companyId: Int
)
