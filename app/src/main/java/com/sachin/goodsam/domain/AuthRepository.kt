package com.sachin.goodsam.domain.repository

interface AuthRepository {
    suspend fun registerDevice(): Result<Unit>

    suspend fun signIn(mobile: String): Result<String>
    suspend fun verifyOtp(mobile: String, otp: String, password: String): Result<Unit>
}