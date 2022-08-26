package com.example.shemajamebeli6.network.repository

import com.example.shemajamebeli6.model.LoginResponse
import com.example.shemajamebeli6.model.RegisterResponse
import com.example.shemajamebeli6.network.RetrofitInstance
import com.example.shemajamebeli6.utils.DataStoreHandler
import retrofit2.Response


class Repository {
    suspend fun postLoginData(loginData: LoginResponse.LoginData): Response<LoginResponse> {
        return RetrofitInstance.getAuthApi().logUser(loginData)
    }

    suspend fun postRegisterData(registerData: RegisterResponse.RegisterData): Response<RegisterResponse> {
        return RetrofitInstance.getAuthApi().registerUser(registerData)
    }
    suspend fun save(key: String, value: String) {
        DataStoreHandler.save(key, value)
    }

    suspend fun clear(){
        DataStoreHandler.clear()
    }

    suspend fun read(key: String): String?{
        return DataStoreHandler.read(key)
    }
}