package com.sachin.goodsam.data.repository

import com.sachin.goodsam.data.DeviceInfoProvider
import com.sachin.goodsam.data.remote.ApiService
import com.sachin.goodsam.data.remote.dto.*
import com.sachin.goodsam.domain.repository.AuthRepository
import com.sachin.goodsam.util.JsonUtil

class AuthRepositoryImpl(
    private val api: ApiService,
    private val device: DeviceInfoProvider
) : AuthRepository {


    private var uniqueId: String = "0"

    override suspend fun registerDevice(): Result<Unit> = runCatching {
        val res = api.registerDevice(
            DeviceRequest(
                deviceId = device.deviceId,
                osVersion = device.osVersion,
                appVersion = device.appVersion,
                modelName = device.modelName
            )
        )
        android.util.Log.d("DEVICE", res.toString())
        JsonUtil.findString(res, "uniqueId")?.let { uniqueId = it }
        Unit
    }

    override suspend fun signIn(mobile: String): Result<String> = runCatching {
        val res = api.signIn(SignInRequest(mobile = mobile))
        android.util.Log.d("SIGNIN", res.toString())


        val serverError = res.get("error")
            ?.takeIf { it.isJsonObject }
            ?.asJsonObject?.get("message")
            ?.takeIf { it.isJsonPrimitive }?.asString
        if (!serverError.isNullOrEmpty()) throw IllegalStateException(serverError)


        JsonUtil.findString(res, "password")
            ?: JsonUtil.findString(res, "otp")
            ?: throw IllegalStateException("Password was not received in the response")
    }

    override suspend fun verifyOtp(mobile: String, otp: String, password: String): Result<Unit> =
        runCatching {
            val res = api.checkOtp(
                CheckOtpRequest(
                    mobile = mobile,
                    otp = otp,
                    password = "",
                    otpPass = "1",
                    uniqueId = uniqueId
                )
            )
            android.util.Log.d("CHECKOTP", res.toString())

            val serverError = res.get("error")
                ?.takeIf { it.isJsonObject }
                ?.asJsonObject?.get("message")
                ?.takeIf { it.isJsonPrimitive }?.asString
            if (!serverError.isNullOrEmpty()) throw IllegalStateException(serverError)
            Unit
        }
}