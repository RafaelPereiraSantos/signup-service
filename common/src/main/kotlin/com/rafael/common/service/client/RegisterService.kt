package com.rafael.common.service.client

import com.rafael.common.models.EndUser
import com.rafael.common.models.EndUserRegister
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterService {
    @POST("register")
    fun register(@Body endUserRegister: EndUserRegister): Call<EndUser>
}