package com.example.mystoryappcompose.data

import android.content.Context
import com.example.mystoryappcompose.data.local.StoryDatabase
import com.example.mystoryappcompose.data.network.ApiConfig
import com.example.mystoryappcompose.preferences.AuthTokenManager

interface AppContainer {
    val myStoryRepository: MyStoryRepository
}

class DefaultAppContainer(
    private val token: String,
    private val authTokenManager: AuthTokenManager,
    context: Context
) : AppContainer {


    override val myStoryRepository: MyStoryRepository by lazy {
        MyStoryRepositoryImpl(
            ApiConfig.getApiService(token),
            StoryDatabase.getDatabase(context),
            authTokenManager
        )
    }


}