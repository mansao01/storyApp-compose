package com.example.mystoryappcompose.data

import com.example.mystoryappcompose.data.local.StoryDatabase
import com.example.mystoryappcompose.data.network.ApiService
import com.example.mystoryappcompose.preferences.AuthTokenManager

interface AppContainer {
    val myStoryRepository: MyStoryRepository
}

class DefaultAppContainer(
    private val apiService: ApiService,
    private val authTokenManager: AuthTokenManager,
    private val storyDatabase: StoryDatabase,
) : AppContainer {


    override val myStoryRepository: MyStoryRepository by lazy {
        MyStoryRepositoryImpl(
            apiService,
            storyDatabase,
            authTokenManager
        )
    }


}