package com.rafael.apiwebflux.service

import com.rafael.apiwebflux.config.CompanyMemberService
import com.rafael.apiwebflux.config.EligibleService
import com.rafael.models.Eligible
import com.rafael.models.SearchResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import retrofit2.Response

@Service
class EligibleSearchService(
    private val eligibleServce: EligibleService,
    private val companyMemberService: CompanyMemberService
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun searchBy(email: String?, token: String?, personalDocument: String?): SearchResult {
        println("asdasdasd")
        val eligibles = eligibleServce.getEligibles(email, token, personalDocument)
        val response = eligibles.execute()
        return handleResponse(response)
    }

    suspend fun coSearchBy(email: String?, token: String?, personalDocument: String?): SearchResult {
        val response = eligibleServce.coGetEligibles(email, token, personalDocument)
        return handleResponse(response)
    }

    private fun handleResponse(response: Response<Eligible>): SearchResult {
        println(response.code().toString())
        println(response.body().toString())
        return when (response.code()) {
            200 -> SearchResult(listOf(response.body()!!))
            404 -> SearchResult()
            400 -> throw IllegalStateException(response.message())
            401 -> throw IllegalStateException(response.message())
            else -> throw Exception(response.code().toString())
        }
    }

}
