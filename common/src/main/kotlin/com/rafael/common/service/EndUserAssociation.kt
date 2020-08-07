package com.rafael.common.service

import com.rafael.common.models.Eligible
import retrofit2.Response
import java.lang.Exception

class EndUserAssociation {

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
