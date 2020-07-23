package com.rafael.service.client

import com.rafael.models.EndUser
import com.rafael.models.EndUserRegister
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterService {
    @POST("register")
    fun register(@Body endUserRegister: EndUserRegister): Call<EndUser>
}