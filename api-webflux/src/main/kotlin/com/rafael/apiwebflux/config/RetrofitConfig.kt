package com.rafael.apiwebflux.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.rafael.models.CompanyMember
import com.rafael.models.Eligible
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface EligibleService {
    @GET("/eligibles")
    fun getEligibles(
        @Query("email_address") email: String?,
        @Query("employee_id") token: String?,
        @Query("document") personalDocument: String?
    ): Call<Eligible>

    @GET("/eligibles")
    suspend fun coGetEligibles(
        @Query("email_address") email: String?,
        @Query("employee_id") token: String?,
        @Query("document") personalDocument: String?
    ): Response<Eligible>
}

interface CompanyMemberService {
    fun getCompanyMembers(
        @Query("email_address") email: String,
        @Query("company_member_token") token: String,
        @Query("personal_document") personalDocument: String
    ): Call<CompanyMember>
}

@Configuration
class RetrofitConfig(
    private val clientConfig: ClientConfig
) {

    @Bean
    fun createEligibleService(
        objectMapper: ObjectMapper
    ): EligibleService {
        return createRetrofitService(
            objectMapper, "eligible", EligibleService::class.java
        )
    }

    @Bean
    fun createCompanyMemberService(
        objectMapper: ObjectMapper
    ): CompanyMemberService {
        return createRetrofitService(
            objectMapper, "companyMember", CompanyMemberService::class.java
        )
    }

    private fun <T> createRetrofitService(
        objectMapper: ObjectMapper,
        clientName: String,
        retrofitInterface: Class<T>
    ): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(clientConfig.clients.getOrThrow(clientName).baseUrl)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()
        return retrofit.create(retrofitInterface)
    }
}

private fun <K, V> Map<K, V>.getOrThrow(key: K): V =
    this[key] ?: throw IllegalStateException("missing property: $key")
