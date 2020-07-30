package com.rafael.apiwebflux.service

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.rafael.apiwebflux.config.CompanyMemberService
import com.rafael.apiwebflux.config.EligibleService
import com.rafael.models.CompanyMember
import com.rafael.models.Eligible
import com.rafael.models.SearchResult
import org.springframework.stereotype.Service
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.Query
import java.util.logging.Level
import java.util.logging.Logger

@Service
class EligibleSearchService(
    private val eligibleServce: EligibleService,
    private val companyMemberService: CompanyMemberService
) {
    fun searchBy(email: String, token: String, personalDocument: String): SearchResult? {
        try {
            val response = eligibleServce.getEligibles(email, token, personalDocument).execute()
            println("-----------------------------")
            println(response.code())
            return when (response.code()) {
                200 -> {
                    val resp = SearchResult(listOf(response.body()!!))
                    println(resp)
                    return resp
                }
                400 -> TODO()
                401 -> TODO()
                404 -> null
                else -> TODO()
            }
        } catch (e: Exception) {
            Logger.getGlobal().log(Level.WARNING, e.message + "something wrong happend")
            return null
        }
    }
}
