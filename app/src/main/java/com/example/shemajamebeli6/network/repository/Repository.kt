package com.example.shemajamebeli6.network.repository

import com.example.shemajamebeli6.model.LoginResponse
import com.example.shemajamebeli6.model.RegisterResponse
import com.example.shemajamebeli6.network.RetrofitInstance
import retrofit2.Response

class Repository {
    suspend fun postLoginData(loginData: LoginResponse.LoginData): Response<LoginResponse> {
        return RetrofitInstance.getAuthApi().logUser(loginData)
    }

    suspend fun postRegisterData(registerData: RegisterResponse.RegisterData): Response<RegisterResponse> {
        return RetrofitInstance.getAuthApi().registerUser(registerData)
    }
}