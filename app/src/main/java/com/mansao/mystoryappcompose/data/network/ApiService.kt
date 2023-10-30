package com.mansao.mystoryappcompose.data.network

import com.mansao.mystoryappcompose.data.network.response.GetStoriesResponse
import com.mansao.mystoryappcompose.data.network.response.GetStoriesWithLocationResponse
import com.mansao.mystoryappcompose.data.network.response.LoginResponse
import com.mansao.mystoryappcompose.data.network.response.PostStoryResponse
import com.mansao.mystoryappcompose.data.network.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email")
        email: String,
        @Field("password")
        password: String
    ): LoginResponse

    @FormUrlEncoded
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
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): GetStoriesResponse

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Query("location") location: Int = 1
    ): GetStoriesWithLocationResponse

    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Part file: MultipartBody.Part,
        @Part("description")
        description: RequestBody,
        @Part("lat") lat:Float?,
        @Part("lon") lon:Float?,
    ): PostStoryResponse

//    Note: not need to specify the headers anymore, because already handled by interceptor, check the app container

}