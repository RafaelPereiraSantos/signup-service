package com.rafael.service

import com.rafael.models.EligibleCreatedEvent
import com.rafael.models.EndUser
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.engine.apache.Apache
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import java.lang.Exception

object EndUserAssociation {

    private const val CORE_URL = "http://localhost:3001/associate"

    fun associate(endUser: EndUser) {
        runBlocking {
            val response = postAssociation(formatPayload(endUser))
            when (response.status) {
                HttpStatusCode.Created -> println("associated")
                HttpStatusCode.Unauthorized -> throw Exception("core.unauthorized")
                else -> {
                    println(response.status)
                    throw Exception("core.response.${response.status}")
                }
            }
        }
    }

    private fun formatPayload(endUser: EndUser): String {
        return(endUser.toString())
    }

    private suspend fun postAssociation(payload: String): HttpResponse {
        val client = HttpClient(Apache) {
            engine {
                followRedirects = true
                connectTimeout = 10_000
            }
        }
        val response = client.post<HttpResponse> {
            url(CORE_URL)
            header("auth", "secretAuth")
            body = payload
        }
        client.close()
        return response
    }
}
