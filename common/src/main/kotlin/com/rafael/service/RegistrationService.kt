package com.rafael.service

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.rafael.models.EndUser
import com.rafael.models.EndUserRegister
import com.rafael.models.ExceptionMessage
import com.rafael.service.client.RegisterService
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.logging.Level
import java.util.logging.Logger

class RegistrationService (
    private val registerHost: String = "http://localhost:3001/"
) {

    private val registerService = createRegisterService()

    fun register(endUserRegister: EndUserRegister) = postRegister(endUserRegister)

    private fun postRegister(endUserRegister: EndUserRegister): EndUser? {
        val result = registerService.register(endUserRegister).execute()

        return when(result.code()) {
            201 -> result.body()?: throw IllegalStateException("No end user was returned")
            404 -> null
            401 -> {
                val message = ExceptionMessage("Unauthorized response from register service", "unauthorized.server2server.request")
                throw com.rafael.models.HttpException(401, message)
            }
            else -> {
                println("Logger: register service response ${result.code()}")
                throw IllegalStateException("Something unexpected ocurred")
            }
        }
    }

    private fun createRegisterService(): RegisterService {
        val objectMapper = createMapper()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .baseUrl(registerHost).build()

        return retrofit.create(RegisterService::class.java)
    }

    private fun createMapper() =
        jacksonObjectMapper().apply {
            propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        }
}
