package com.example.mystoryappcompose.data

import com.example.mystoryappcompose.data.network.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val myStoryRepository: MyStoryRepository
}

class DefaultAppContainer : AppContainer {

    private val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
    override val myStoryRepository: MyStoryRepository by lazy {
        NetworkMyStoryRepository(retrofitService)
    }

    companion object {
        private const val BASE_URL = "https://story-api.dicoding.dev/v1/"
    }
}