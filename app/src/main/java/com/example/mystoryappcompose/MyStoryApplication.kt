package com.example.mystoryappcompose

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.mystoryappcompose.data.AppContainer
import com.example.mystoryappcompose.data.DefaultAppContainer

private const val AUTH_PREF_NAME = "auth_pref"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = AUTH_PREF_NAME
)
class MyStoryApplication:Application() {
    lateinit var container:AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()

    }
}