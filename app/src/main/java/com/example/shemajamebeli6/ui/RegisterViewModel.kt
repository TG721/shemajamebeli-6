package com.example.tbc_homework15.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shemajamebeli6.model.RegisterResponse
import com.example.shemajamebeli6.network.repository.Repository
import com.example.tbc_homework15.utils.ResponseState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class RegisterViewModel(private val repository: Repository) : ViewModel() {
    private val _registerResponseState =
        MutableStateFlow<ResponseState<RegisterResponse>>(ResponseState.Empty()) //mutable
    val registerResponseState = _registerResponseState.asStateFlow()

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _registerResponseState.emit(ResponseState.Loading())
            val response: Response<RegisterResponse> = repository.postRegisterData(
                RegisterResponse.RegisterData(
                    email,
                    password
                )
            )
            try {
                val body: RegisterResponse? = response.body()
                if (response.isSuccessful && response.body() != null) {
                    _registerResponseState.emit(ResponseState.Success(body!!))
                } else {
                    _registerResponseState.emit(
                        ResponseState.Error(
                            response.errorBody()?.string()
                        )
                    )
                }
            } catch (e: Throwable) {
                _registerResponseState.emit(ResponseState.Error(message = e.message ?: "error"))
            }

        }
    }
}