package com.example.mystoryappcompose.data

import com.example.mystoryappcompose.data.network.ApiService
import com.example.mystoryappcompose.data.network.response.GetStoriesResponse
import com.example.mystoryappcompose.data.network.response.LoginResponse
import com.example.mystoryappcompose.data.network.response.RegisterResponse

interface MyStoryRepository {
    suspend fun register(name: String, email: String, password: String): RegisterResponse
    suspend fun login(email: String, password: String): LoginResponse
    suspend fun getStories(token: String): GetStoriesResponse
}

class NetworkMyStoryRepository(
    private val apiService: ApiService
) : MyStoryRepository {
    override suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    override suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    override suspend fun getStories(token: String): GetStoriesResponse {
        return apiService.getStories(token)
    }
}