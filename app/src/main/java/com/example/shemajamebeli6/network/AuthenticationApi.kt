package com.example.shemajamebeli6.network

import com.example.shemajamebeli6.model.LoginResponse
import com.example.shemajamebeli6.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationApi {
    @POST("login")
    suspend fun logUser(@Body user: LoginResponse.LoginData): Response<LoginResponse>

    @POST("register")
    suspend fun registerUser(@Body user: RegisterResponse.RegisterData): Response<RegisterResponse>

}