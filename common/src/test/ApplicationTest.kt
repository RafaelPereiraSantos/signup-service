package com.rafael

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.rafael.http.Api
import com.rafael.http.serviceConfiguration
import io.ktor.application.Application
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() {
        val endUserCompanies = Api()
        withTestApplication(Application::serviceConfiguration) {
            handleRequest(HttpMethod.Get, "/health").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val mapper = ObjectMapper()
                val node: JsonNode = mapper.readTree(response.content)
                assertEquals("OK", node["status"].textValue())
            }
        }
    }
}
