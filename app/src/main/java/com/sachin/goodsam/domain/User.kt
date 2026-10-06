package com.sachin.goodsam.domain.model

data class User(
    val id: Int,
    val fullName: String,
    val email: String,
    val phone: String,
    val imageUrl: String,
    val company: String
)