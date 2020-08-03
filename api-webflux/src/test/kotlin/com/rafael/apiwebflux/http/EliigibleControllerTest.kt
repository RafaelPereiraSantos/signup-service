package com.rafael.apiwebflux.http

import ch.qos.logback.core.net.server.Client
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.rafael.apiwebflux.config.*
import com.rafael.apiwebflux.service.EligibleSearchService
import com.rafael.models.Eligible
import com.rafael.models.SearchResult
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import retrofit2.Call
import retrofit2.Response

@ContextConfiguration(
    classes = [
        EligibleSearchService::class,
        EligibleController::class,
        RetrofitConfig::class,
        ClientConfig::class,
        ServiceConfiguration::class
    ]
)
@WebFluxTest(EligibleController::class)
class EliigibleControllerTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockBean
    lateinit var eligibleService: EligibleService

    @Test
    fun `when search eligibles in controller with valid parameters`() {
        val email = "abc@email.com"
        val companyMemberToken = "abc123"
        val personalDocument = "abc1234"
        val expectedEligible = Eligible(email, companyMemberToken, personalDocument, 1)

        val call = mock<Call<Eligible>> {
            on { it.execute() } doReturn Response.success(expectedEligible)
        }

        Mockito.`when`(
            eligibleService.getEligibles(any(), any(), any())
        ).thenReturn(call)

        val params = listOf(
            "email" to expectedEligible.emailAddress,
            "company_member_token" to expectedEligible.employeeId,
            "personal_document" to expectedEligible.personalDocument,
            "company_id" to expectedEligible.companyId
        ).joinToString("&", "?") { (key, value) -> "$key=$value" }

        val result = webTestClient
            .get()
            .uri("/web-flux-controller/eligibility$params")
            .exchange()

        assertAll(
            { result.expectStatus().isOk },
            {
                result.expectBody(Eligible::class.java)
                    .isEqualTo<Nothing>(expectedEligible)
            }
        )
    }


    fun createEligible(
        email: String = "email@email",
        token: String = "abc123",
        personalDocument: String = "abc1235",
        companyId: Int = 1
    ) = Eligible(email, token, personalDocument, companyId)

    fun createSearchResult(vararg eligibles: Eligible): SearchResult {
        return SearchResult(eligibles.toList())
    }
}
