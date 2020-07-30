package com.rafael.apiwebflux.http

import com.rafael.models.Eligible
import com.rafael.service.EligibilitySearch
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.test.web.reactive.server.WebTestClient

class EligibleRoutesTest {

    @Test
    fun `when search eligibles with all parameters it should return a list of valid result`() {
        val client = WebTestClient
            .bindToRouterFunction(eligibiltyRoute(EligibilitySearch()))
            .build()

        val result = client.get()
            .uri("/eligibility")
            .exchange()
        assertAll(
            { result.expectStatus().isBadRequest },
            {
                result.expectBody(Eligible::class.java)
                    .isEqualTo<Nothing>(Eligible("teste@emailfake.com", "a", "a"))
            }
        )
    }
}
