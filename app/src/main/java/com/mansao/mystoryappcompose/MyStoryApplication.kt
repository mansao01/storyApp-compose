package com.mansao.mystoryappcompose

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
import com.mansao.mystoryappcompose.data.AppContainer
import com.mansao.mystoryappcompose.data.DefaultAppContainer
import com.mansao.mystoryappcompose.data.local.StoryDatabase
import com.mansao.mystoryappcompose.data.network.ApiConfig
import com.mansao.mystoryappcompose.preferences.AuthTokenManager
import kotlinx.coroutines.runBlocking

private const val AUTH_PREF_NAME = "auth_pref"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = AUTH_PREF_NAME
)

class MyStoryApplication : Application(), ImageLoaderFactory {
    lateinit var container: AppContainer
    lateinit var authTokenManager: AuthTokenManager
    override fun onCreate() {
        super.onCreate()
        authTokenManager = AuthTokenManager(dataStore)
        val token = runBlocking { authTokenManager.getAccessToken() }
        val apiService = ApiConfig.getApiService(token.toString())
        val storyDatabase = StoryDatabase.getDatabase(this)
        container = DefaultAppContainer(
            apiService = apiService,
            storyDatabase = storyDatabase,
            authTokenManager = authTokenManager
        )

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