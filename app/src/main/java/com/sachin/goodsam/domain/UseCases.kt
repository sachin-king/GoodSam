package com.sachin.goodsam.domain.usecase

import com.sachin.goodsam.domain.model.User
import com.sachin.goodsam.domain.repository.AuthRepository
import com.sachin.goodsam.domain.repository.UserRepository

class RegisterDeviceUseCase(private val repo: AuthRepository) {
    suspend operator fun invoke(): Result<Unit> = repo.registerDevice()
}

class SignInUseCase(private val repo: AuthRepository) {
    suspend operator fun invoke(mobile: String): Result<String> = repo.signIn(mobile)
}

class VerifyOtpUseCase(private val repo: AuthRepository) {
    suspend operator fun invoke(mobile: String, otp: String, password: String): Result<Unit> =
        repo.verifyOtp(mobile, otp, password)
}

class GetUsersUseCase(private val repo: UserRepository) {
    suspend operator fun invoke(): Result<List<User>> = repo.getUsers()
}