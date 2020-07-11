package com.rafael.service

import com.rafael.models.EligibleCreatedEvent
import com.rafael.models.EndUser
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.util.logging.Logger

object Association {

    private const val CORE_URL = "http://fake-core.com/associate"

    fun associate(endUser: EndUser) {
        try {
            runBlocking {
                val response = postAssociation(formatPayload(endUser))
                when (response.status) {
                    HttpStatusCode.Created -> {
                        println("associated")
                    }
                    HttpStatusCode.Unauthorized -> {
                        println("unauthorized")
                    }
                    else -> {
                        println(response.status)
                    }
                }
            }
        } catch (e: Exception) {
            println(e)
        }
    }

    fun associate(eligibleEvent: EligibleCreatedEvent) {
        associate(EndUser(eligibleEvent))
    }

    private fun formatPayload(endUser: EndUser): String {
        return("")
    }

    private suspend fun postAssociation(payload: String): HttpResponse {
        val client = HttpClient()
        return client.post<HttpResponse> {
            url(CORE_URL)
            header("auth", "secretAuth")
            body = payload
        }
    }
}
