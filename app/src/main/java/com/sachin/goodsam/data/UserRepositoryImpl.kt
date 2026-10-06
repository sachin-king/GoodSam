package com.sachin.goodsam.data.repository

import com.sachin.goodsam.data.remote.ApiService
import com.sachin.goodsam.data.remote.dto.toDomain
import com.sachin.goodsam.domain.model.User
import com.sachin.goodsam.domain.repository.UserRepository

class UserRepositoryImpl(private val api: ApiService) : UserRepository {
    override suspend fun getUsers(): Result<List<User>> = runCatching {
        api.getUsers().users.map { it.toDomain() }
    }
}