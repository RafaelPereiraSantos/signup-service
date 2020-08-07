package com.rafael.apiwebflux.http

import com.nhaarman.mockitokotlin2.*
import com.rafael.apiwebflux.service.EligibleSearchService
import com.rafael.common.models.Eligible
import com.rafael.common.models.SearchResult
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@ExtendWith(MockitoExtension::class)
class EligibleRoutesTest {

    @Mock
    lateinit var eligibleSearchService: EligibleSearchService

    @Test
    fun `when search eligibles with all parameters it should return a valid eligible`() {
        val email = "abc@email.com"
        val companyMemberToken = "abc123"
        val personalDocument = "abc1234"

        val expectedEligible = Eligible(email, companyMemberToken, personalDocument, 1)

        Mockito.`when`(
            eligibleSearchService.reactorSearchBy(email, companyMemberToken, personalDocument)
        ) doReturn searchResultMonoWith(expectedEligible)

        val client = WebTestClient
            .bindToRouterFunction(coEligibiltyRoute(eligibleSearchService))
            .build()

        val result = client.get()
            .uri(
                "/eligibility?email=$email&company_member_token=$companyMemberToken&personal_document=$personalDocument"
            )
            .exchange()
        assertAll(
            { result.expectStatus().isOk },
            {
                result.expectBody(Eligible::class.java)
                    .isEqualTo<Nothing>(expectedEligible)
            }
        )
    }

    @Test
    fun `when search eligible with random parameters it should return a valid eligible`() {
        val email = "abc@email.com"
        val companyMemberToken = "abc123"
        val personalDocument = "abc1234"

        val expectedEligible = Eligible(email, companyMemberToken, personalDocument, 1)

        val eligibleSearchServiceMock = mock<EligibleSearchService> {
            on { it.reactorSearchBy(any(), any(), any()) } doReturn searchResultMonoWith(expectedEligible)
        }

        val client = WebTestClient
            .bindToRouterFunction(coEligibiltyRoute(eligibleSearchServiceMock))
            .build()

        val result = client.get()
            .uri(
                "/eligibility?email=$email&company_member_token=$companyMemberToken&personal_document=$personalDocument"
            )
            .exchange()
        assertAll(
            { result.expectStatus().isOk },
            {
                result.expectBody(Eligible::class.java)
                    .isEqualTo<Nothing>(expectedEligible)
            }
        )
    }

    @Test
    fun `when search eligible with valid parameters it should call reactorSearchBy one time`() {
        val email = "abc@email.com"
        val companyMemberToken = "abc123"
        val personalDocument = "abc1234"

        val expectedEligible = Eligible(email, companyMemberToken, personalDocument, 1)

        val eligibleSearchServiceMock = mock<EligibleSearchService> {
            on { it.reactorSearchBy(any(), any(), any()) } doReturn searchResultMonoWith(expectedEligible)
        }

        val client = WebTestClient
            .bindToRouterFunction(coEligibiltyRoute(eligibleSearchServiceMock))
            .build()

        client.get()
            .uri(
                "/eligibility?email=$email&company_member_token=$companyMemberToken&personal_document=$personalDocument"
            ).exchange()

        verify(eligibleSearchServiceMock).reactorSearchBy(any(), any(), eq(personalDocument))

    }

    @Test
    fun `when search eligible with valid parameters it should pass certain parameters`() {
        val expectedEligible = createEligible()

        val emailCaptor = argumentCaptor<String>()

        val eligibleSearchServiceMock = mock<EligibleSearchService> {
            on { reactorSearchBy(emailCaptor.capture(), any(), any()) } doReturn searchResultMonoWith(expectedEligible)
        }

        val client = WebTestClient
            .bindToRouterFunction(coEligibiltyRoute(eligibleSearchServiceMock))
            .build()

        val params = listOf(
            "email" to expectedEligible.emailAddress,
            "company_member_token" to expectedEligible.employeeId,
            "personal_document" to expectedEligible.personalDocument,
            "company_id" to expectedEligible.companyId
        ).joinToString("&", "?") { (key, value) -> "$key=$value" }

        client.get()
            .uri(
                "/eligibility$params"
            ).exchange()

        assertEquals(expectedEligible.emailAddress, emailCaptor.firstValue)
    }

    fun createEligible(
        email: String = "email@email",
        token: String = "abc123",
        personalDocument: String = "abc1235",
        companyId: Int = 1
    ) = Eligible(email, token, personalDocument, companyId)

    fun searchResultMonoWith(vararg eligibles: Eligible) = Mono.just(SearchResult(eligibles.toList()))
}
