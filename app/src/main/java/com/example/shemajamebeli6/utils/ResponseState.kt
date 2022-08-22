package com.example.tbc_homework15.utils

sealed class ResponseState<T> {
    data class Success<T>(val item: T) : ResponseState<T>()
    data class Error<T>(val message: String?) : ResponseState<T>()
    class Loading<T> : ResponseState<T>()
    class Empty<T> : ResponseState<T>()
}