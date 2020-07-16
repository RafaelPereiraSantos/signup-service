package service;

import com.fasterxml.jackson.databind.ObjectMapper
import com.rafael.models.EndUser
import com.rafael.models.EndUserRegister
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import retrofit.converter.JacksonConverter
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface RegisterService {
    @POST("register")
    fun register(@Body endUserRegister: EndUserRegister): EndUser
}

class RegistrationService (
    private val registerHost: String = "http://localhost:3001/"
) {

    fun register(endUserRegister: EndUserRegister): EndUser {

        val response = postRegister(endUserRegister)
        when (response.status) {
            HttpStatusCode.Created -> {
                response.content
                return
            }
            HttpStatusCode.Unauthorized -> throw Exception("core.unauthorized")
            else -> {
                println(response.status)
                throw Exception("core.response.${response.status}")
            }
        }
    }

    private fun postRegister(endUserRegister: EndUserRegister): HttpResponse {
        val objectMapper = ObjectMapper.
        val retrofit = Retrofit.Builder().addConverterFactory(JacksonConverter) .baseUrl(registerHost).build()
        val service = retrofit.create(RegisterService::class.java)
        val newEndUser = service.register(endUserRegister)
        return newEndUser
    }
}
