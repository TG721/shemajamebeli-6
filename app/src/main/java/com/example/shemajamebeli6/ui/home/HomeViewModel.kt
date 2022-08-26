package com.example.shemajamebeli6.ui.home

import androidx.lifecycle.ViewModel
import com.example.shemajamebeli6.network.repository.Repository

class HomeViewModel(private val repository: Repository): ViewModel() {
    suspend fun clear(){
        repository.clear()
    }
    suspend fun getEmail(key: String):String? {
        return repository.read(key)
    }

}