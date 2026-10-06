package com.sachin.goodsam.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DeviceRequest(
    val uniqueId: String = "0",
    val deviceId: String,
    val osVersion: String,
    val osName: String = "Android",
    val devicePlatform: String = "Android",
    val appVersion: String,
    val deviceTimezone: String = "",
    val deviceCurrentTimestamp: String = "",
    val token: String = "",
    val modelName: String
)

data class SignInRequest(
    val mobile: String,
    val otpPass: String = "1",
    val smsAutofill: String = "",
    @SerializedName("user_reg_goodsam_status")
    val userRegGoodsamStatus: String = "1"
)

data class CheckOtpRequest(
    val mobile: String,
    val otp: String,
    val password: String,
    val otpPass: String = "",
    val uniqueId: String,
    @SerializedName("user_reg_goodsam_status")
    val userRegGoodsamStatus: String = "1"
)