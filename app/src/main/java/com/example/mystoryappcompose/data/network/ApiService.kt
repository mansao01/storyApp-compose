package com.example.mystoryappcompose.data.network

import com.example.mystoryappcompose.data.network.response.GetStoriesResponse
import com.example.mystoryappcompose.data.network.response.LoginResponse
import com.example.mystoryappcompose.data.network.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    suspend fun login(
        @Field("email")
        email: String,
        @Field("password")
        password: String
    ): LoginResponse

    @POST("register")
    suspend fun register(
        @Field("name")
        name: String,
        @Field("email")
        email: String,
        @Field("password")
        password: String
    ): RegisterResponse

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") header: String
    ):GetStoriesResponse
}