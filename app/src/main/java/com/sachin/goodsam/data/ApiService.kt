package com.sachin.goodsam.data.remote

import com.google.gson.JsonObject
import com.sachin.goodsam.data.remote.dto.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("SPEROEMS2024/communityapp/device")
    suspend fun registerDevice(@Body body: DeviceRequest): JsonObject

    @POST("SPEROEMS2024/communityapp/signin")
    suspend fun signIn(@Body body: SignInRequest): JsonObject

    @POST("SPEROEMS2024/communityapp/checkotp")
    suspend fun checkOtp(@Body body: CheckOtpRequest): JsonObject

    @GET("https://dummyjson.com/users")
    suspend fun getUsers(): UsersResponse
}