package com.rafael.service

import com.rafael.models.Eligible
import retrofit2.Response
import java.lang.Exception

object EndUserAssociation {

    private const val CORE_URL = "http://localhost:3001/associate"

    fun associate(eligible: Eligible) {
        val response = postAssociation(formatPayload(eligible))
        when (response.code()) {
            201 -> println("associated")
            401 -> throw Exception("core.unauthorized")
            else -> {
                println(response.code())
                throw Exception("core.response.${response.code()}")
            }
        }
    }

    private fun formatPayload(eligible: Eligible): String {
        return(eligible.toString())
    }

    private fun postAssociation(payload: String): Response<*> = TODO() //{
//        val client = HttpClient(Apache) {
//            engine {
//                followRedirects = true
//                connectTimeout = 10_000
//            }
//        }
//        val response = client.post<HttpResponse> {
//            url(CORE_URL)
//            header("auth", "secretAuth")
//            body = payload
//        }
//        client.close()
//        return response
//    }
}
