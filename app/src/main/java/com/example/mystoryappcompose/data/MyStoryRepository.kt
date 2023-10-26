package com.example.mystoryappcompose.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.mystoryappcompose.data.local.StoryDatabase
import com.example.mystoryappcompose.data.network.ApiConfig
import com.example.mystoryappcompose.data.network.ApiService
import com.example.mystoryappcompose.data.network.response.GetStoriesWithLocationResponse
import com.example.mystoryappcompose.data.network.response.ListStoryItem
import com.example.mystoryappcompose.data.network.response.LoginResponse
import com.example.mystoryappcompose.data.network.response.PostStoryResponse
import com.example.mystoryappcompose.data.network.response.RegisterResponse
import com.example.mystoryappcompose.data.paging.StoryRemoteMediator
import com.example.mystoryappcompose.preferences.AuthTokenManager
import com.example.mystoryappcompose.utils.CameraUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

interface MyStoryRepository {
    suspend fun register(name: String, email: String, password: String): RegisterResponse
    suspend fun login(email: String, password: String): LoginResponse

    //    suspend fun getStories(): GetStoriesResponse
    suspend fun getStories(): Flow<PagingData<ListStoryItem>>
    suspend fun getStoriesWithLocation(): GetStoriesWithLocationResponse
    suspend fun postStory(
        getFile: File,
        description: String,
        lat: Float? = null,
        lon: Float? = null
    ): PostStoryResponse
}

class MyStoryRepositoryImpl(
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase,
    private val authTokenManager: AuthTokenManager
) : MyStoryRepository {
    override suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    override suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getStories(): Flow<PagingData<ListStoryItem>> {
        val pagingSourceFactory = { storyDatabase.storyDao().getAllStories() }
        val token = runBlocking {
            authTokenManager.getAccessToken()
        }
        val apiService = ApiConfig.getApiService(token.toString())
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = StoryRemoteMediator(
                apiService = apiService,
                storyDatabase = storyDatabase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override suspend fun getStoriesWithLocation(): GetStoriesWithLocationResponse {
        val token = runBlocking {
            authTokenManager.getAccessToken()
        }
        val apiService = ApiConfig.getApiService(token.toString())
        return apiService.getStoriesWithLocation()
    }

    override suspend fun postStory(
        getFile: File, description: String, lat: Float?, lon: Float?
    ): PostStoryResponse {
        val file = CameraUtils.reduceFileImage(getFile)
        val descBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )
        val token = runBlocking {
            authTokenManager.getAccessToken()
        }
        val apiService = ApiConfig.getApiService(token.toString())
        return apiService.postStory(imageMultipart, descBody, lat, lon)
    }
}