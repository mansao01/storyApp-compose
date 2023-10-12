package com.example.mystoryappcompose.ui.common

import com.example.mystoryappcompose.data.network.response.LoginResponse

sealed interface LoginUiState {
    object StandBy : LoginUiState
    object Loading : LoginUiState
    data class Success(val loginResponse: LoginResponse) : LoginUiState
    data class Error(val msg: String) : LoginUiState

}
