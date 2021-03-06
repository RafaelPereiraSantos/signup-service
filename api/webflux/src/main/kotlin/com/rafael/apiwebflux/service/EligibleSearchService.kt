package com.rafael.apiwebflux.service

import com.rafael.apiwebflux.config.CompanyMemberService
import com.rafael.apiwebflux.config.EligibleService
import com.rafael.common.models.Eligible
import com.rafael.common.models.SearchResult
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import retrofit2.Response

@Service
class EligibleSearchService(
    private val eligibleServce: EligibleService,
    private val companyMemberService: CompanyMemberService
) {

    fun searchBy(email: String?, token: String?, personalDocument: String?): SearchResult {
        val eligibles = eligibleServce.getEligibles(email, token, personalDocument)
        val response = eligibles.execute()
        return handleResponse(response)
    }

    suspend fun coSearchBy(email: String?, token: String?, personalDocument: String?): SearchResult {
        val response = eligibleServce.coGetEligibles(email, token, personalDocument)
        return handleResponse(response)
    }

    fun reactorSearchBy(email: String?, token: String?, personalDocument: String?): Mono<SearchResult> {
        return eligibleServce.reactorGetEligibles(email, token, personalDocument).map {
            handleResponse(it)
        }
    }

    private fun handleResponse(response: Response<Eligible>): SearchResult {
        return when (response.code()) {
            200 -> SearchResult(listOf(response.body()!!))
            404 -> SearchResult()
            400, 401 -> throw IllegalStateException(response.message())
            else -> throw Exception(response.code().toString())
        }
    }

}
