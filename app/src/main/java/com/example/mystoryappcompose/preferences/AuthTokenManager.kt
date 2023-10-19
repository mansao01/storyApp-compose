package com.example.mystoryappcompose.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

class AuthTokenManager(private val dataStore: DataStore<Preferences>) {
    private val accessTokenKey = stringPreferencesKey("access_token")
    private val username = stringPreferencesKey("username")
    private val isLoginState = booleanPreferencesKey("is_login")

    suspend fun saveAccessToken(token: String) {
        dataStore.edit { preferences ->
            preferences[accessTokenKey] = token
        }
    }

    suspend fun saveIsLoginState(isLogin: Boolean) {
        dataStore.edit { preferences ->
            preferences[isLoginState] = isLogin
        }
    }

    suspend fun saveUsername(userName:String){
        dataStore.edit { preferences ->
            preferences[username] = userName
        }
    }

    fun getIsLoginState(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val loginState = preferences[isLoginState] ?: false
                loginState
            }
    }

    suspend fun getAccessToken(): String? {
        val preferences = dataStore.data.first()
        return preferences[accessTokenKey]
    }

    suspend fun getUsername():String?{
        val preferences = dataStore.data.first()
        return preferences[username]
    }

    suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences.remove(accessTokenKey)
        }
    }

    suspend fun clearUsername() {
        dataStore.edit { preferences ->
            preferences.remove(username)
        }
    }
}