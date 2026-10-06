package com.sachin.goodsam.domain.repository

import com.sachin.goodsam.domain.model.User

interface UserRepository {
    suspend fun getUsers(): Result<List<User>>
}