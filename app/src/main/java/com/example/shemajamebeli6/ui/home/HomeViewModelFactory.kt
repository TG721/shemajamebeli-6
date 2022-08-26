package com.example.shemajamebeli6.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shemajamebeli6.network.repository.Repository
import com.example.shemajamebeli6.ui.home.HomeViewModel

class HomeViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return (HomeViewModel(repository) as T)
    }
}