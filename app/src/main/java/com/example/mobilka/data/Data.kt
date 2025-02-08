package com.example.mobilka.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "app_prefs")

class PreferencesManager(private val context: Context){
    private val isRegisteredKey = booleanPreferencesKey("isRegistered")

    val isRegistered: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[isRegisteredKey] ?: false}

    suspend fun setRegistered(value: Boolean){
        context.dataStore.edit { preferences ->
            preferences[isRegisteredKey] = value
        }
    }

    suspend fun deleteRegistered(){
        context.dataStore.edit { preferences ->
            preferences.remove(isRegisteredKey)
        }
    }
}