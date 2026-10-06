package com.sachin.goodsam.data.remote.dto

import com.sachin.goodsam.domain.model.User

data class UsersResponse(val users: List<UserDto>)

data class UserDto(
    val id: Int,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val phone: String?,
    val image: String?,
    val company: CompanyDto?
)

data class CompanyDto(val name: String?, val title: String?)

fun UserDto.toDomain() = User(
    id = id,
    fullName = "${firstName.orEmpty()} ${lastName.orEmpty()}".trim(),
    email = email.orEmpty(),
    phone = phone.orEmpty(),
    imageUrl = image.orEmpty(),
    company = listOfNotNull(company?.title, company?.name).joinToString(" @ ")
)