package service;

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.rafael.models.EndUser
import com.rafael.models.EndUserRegister
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterService {
    @POST("register")
    fun register(@Body endUserRegister: EndUserRegister): Call<EndUser>
}

class RegistrationService (
    private val registerHost: String = "http://localhost:3001/"
) {

    private val registerService = createRegisterService()

    fun register(endUserRegister: EndUserRegister) = postRegister(endUserRegister)

    private fun postRegister(endUserRegister: EndUserRegister): EndUser {
        val result = registerService.register(endUserRegister).execute()

        if (!result.isSuccessful) throw IllegalStateException("There were problems retrieving end users")
        return result.body() ?: throw IllegalStateException("No end user was returned")
    }

    private fun createRegisterService(): RegisterService {
        val objectMapper = createMapper()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .baseUrl(registerHost).build()

        return retrofit.create(RegisterService::class.java)
    }

    private fun createMapper() =
        jacksonObjectMapper().apply { propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE }
}
