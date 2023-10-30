package com.mansao.mystoryappcompose.data

import com.mansao.mystoryappcompose.data.local.StoryDatabase
import com.mansao.mystoryappcompose.data.network.ApiService
import com.mansao.mystoryappcompose.preferences.AuthTokenManager

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