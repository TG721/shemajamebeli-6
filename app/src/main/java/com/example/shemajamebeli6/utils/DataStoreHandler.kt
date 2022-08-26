package com.example.shemajamebeli6.utils

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.shemajamebeli6.app.App
import kotlinx.coroutines.flow.first


object DataStoreHandler {
    private val Application.dataStore by preferencesDataStore(name="settings")

    suspend fun read(key: String): String?{
        val dataStoreKey = stringPreferencesKey(key)

        val preferences = App.appContext.dataStore.data.first()
        return preferences[dataStoreKey]
    }

    suspend fun save(key: String, value: String){
        val dataStoreKey = stringPreferencesKey(key)
        App.appContext.dataStore.edit {
            it[dataStoreKey] = value
        }

    }

    suspend fun clear() {
        App.appContext.dataStore.edit {
            it.clear()
        }
    }
}