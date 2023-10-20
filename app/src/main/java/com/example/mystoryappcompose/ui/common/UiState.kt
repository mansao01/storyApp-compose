package com.example.mystoryappcompose.ui.common

import com.example.mystoryappcompose.data.network.response.GetStoriesResponse
import com.example.mystoryappcompose.data.network.response.GetStoriesWithLocationResponse
import com.example.mystoryappcompose.data.network.response.LoginResponse
import com.example.mystoryappcompose.data.network.response.PostStoryResponse
import com.example.mystoryappcompose.data.network.response.RegisterResponse


sealed interface RegisterUiState {
    object StandBy : RegisterUiState
    object Loading : RegisterUiState
    data class Success(val registerResponse: RegisterResponse) : RegisterUiState
    data class Error(val msg: String) : RegisterUiState

}

sealed interface LoginUiState {
    object StandBy : LoginUiState
    object Loading : LoginUiState
    data class Success(val loginResponse: LoginResponse) : LoginUiState
    data class Error(val msg: String) : LoginUiState

}

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val getStoriesResponse: GetStoriesResponse, val username: String) :
        HomeUiState

    data class Error(val msg: String) : HomeUiState

}

sealed interface MapUiState {
    object Loading : MapUiState
    data class Success(val getStoriesWithLocationResponse: GetStoriesWithLocationResponse) :
        MapUiState

    data class Error(val msg: String) : MapUiState

}

sealed interface AddUiState {
    object StandBy : AddUiState
    object Loading : AddUiState
    data class Success(val postStoryResponse: PostStoryResponse) : AddUiState
    data class Error(val msg: String) : AddUiState

}


