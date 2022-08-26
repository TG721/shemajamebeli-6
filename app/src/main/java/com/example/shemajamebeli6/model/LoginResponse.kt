package com.example.shemajamebeli6.model

data class LoginResponse(
    val token: String?
) {
    data class LoginData(
        val email: String?,
        val password: String?
    )
}