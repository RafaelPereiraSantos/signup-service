package com.rafael.apiwebflux.http

import com.nhaarman.mockitokotlin2.*
import com.rafael.apiwebflux.service.EligibleSearchService
import com.rafael.models.Eligible
import com.rafael.models.SearchResult
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.web.reactive.server.WebTestClient

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
            eligibleSearchService.searchBy(email, companyMemberToken, personalDocument)
        ).thenReturn(SearchResult(listOf(expectedEligible)))

        val client = WebTestClient
            .bindToRouterFunction(eligibiltyRoute(eligibleSearchService))
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
            on { it.searchBy(any(), any(), any()) } doReturn SearchResult(listOf(expectedEligible))
        }

        val client = WebTestClient
            .bindToRouterFunction(eligibiltyRoute(eligibleSearchServiceMock))
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
    fun `when search eligible with valid parameters it should call searchBy one time`() {
        val email = "abc@email.com"
        val companyMemberToken = "abc123"
        val personalDocument = "abc1234"

        val expectedEligible = Eligible(email, companyMemberToken, personalDocument, 1)

        val eligibleSearchServiceMock = mock<EligibleSearchService> {
            on { it.searchBy(any(), any(), any()) } doReturn SearchResult(listOf(expectedEligible))
        }

        val client = WebTestClient
            .bindToRouterFunction(eligibiltyRoute(eligibleSearchServiceMock))
            .build()

        client.get()
            .uri(
                "/eligibility?email=$email&company_member_token=$companyMemberToken&personal_document=$personalDocument"
            ).exchange()

        verify(eligibleSearchServiceMock).searchBy(any(), any(), eq(personalDocument))

    }

    @Test
    fun `when search eligible with valid parameters it should pass certain parameters`() {
        val expectedEligible = createEligible()

        val emailCaptor = argumentCaptor<String>()

        val eligibleSearchServiceMock = mock<EligibleSearchService> {
            on { it.searchBy(emailCaptor.capture(), any(), any()) } doReturn SearchResult(listOf(expectedEligible))
        }

        val client = WebTestClient
            .bindToRouterFunction(eligibiltyRoute(eligibleSearchServiceMock))
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

    fun createSearchResult(vararg eligibles: Eligible): SearchResult {
        return SearchResult(eligibles.toList())
    }
}
