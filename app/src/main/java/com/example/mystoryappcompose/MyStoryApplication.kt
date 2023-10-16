package com.example.mystoryappcompose

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.example.mystoryappcompose.data.AppContainer
import com.example.mystoryappcompose.data.DefaultAppContainer
import com.example.mystoryappcompose.preferences.AuthTokenManager

private const val AUTH_PREF_NAME = "auth_pref"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = AUTH_PREF_NAME
)

class MyStoryApplication : Application(), ImageLoaderFactory {
    lateinit var container: AppContainer
    lateinit var authTokenManager: AuthTokenManager
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
        authTokenManager = AuthTokenManager(dataStore)

    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader(this).newBuilder()
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.1)
                    .strongReferencesEnabled(true)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .maxSizePercent(0.03)
                    .directory(cacheDir)
                    .build()
            }
            .logger(DebugLogger())
            .respectCacheHeaders(false)
            .build()
    }
}