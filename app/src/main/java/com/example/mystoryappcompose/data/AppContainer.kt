package com.example.mystoryappcompose.data

import android.content.Context
import com.example.mystoryappcompose.data.local.StoryDatabase
import com.example.mystoryappcompose.data.network.ApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val myStoryRepository: MyStoryRepository
}

class DefaultAppContainer(private val token:String, context:Context) : AppContainer {

    private val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val authInterceptor = Interceptor{chain ->
        val req = chain.request()
        val requestHeader = req.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        chain.proceed(requestHeader)
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
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
        NetworkMyStoryRepository(retrofitService, StoryDatabase.getDatabase(context))
    }

    companion object {
        private const val BASE_URL = "https://story-api.dicoding.dev/v1/"
    }
}