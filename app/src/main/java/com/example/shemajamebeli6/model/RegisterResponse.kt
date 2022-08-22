package com.example.shemajamebeli6.model

data class RegisterResponse(
    val id: Int,
    val s: String?
) {
    data class RegisterData(
        val email: String?,
        val password: String?,
    )
}