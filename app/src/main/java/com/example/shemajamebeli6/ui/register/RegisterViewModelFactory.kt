package com.example.shemajamebeli6.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shemajamebeli6.network.repository.Repository

class RegisterViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return (RegisterViewModel(repository) as T)
    }
}