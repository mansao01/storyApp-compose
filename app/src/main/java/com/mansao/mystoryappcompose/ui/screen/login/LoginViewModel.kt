package com.mansao.mystoryappcompose.ui.screen.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.mystoryappcompose.data.MyStoryRepository
import com.mansao.mystoryappcompose.preferences.AuthTokenManager
import com.mansao.mystoryappcompose.ui.common.LoginUiState
import kotlinx.coroutines.launch

class LoginViewModel(
    private val myStoryRepository: MyStoryRepository,
    private val authTokenManager: AuthTokenManager
) : ViewModel() {
    var uiState: LoginUiState by mutableStateOf(LoginUiState.StandBy)
        private set

    fun getUiState() {
        uiState = LoginUiState.StandBy
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            uiState = LoginUiState.Loading
            uiState = try {
                val result = myStoryRepository.login(email, password)
                authTokenManager.saveAccessToken(result.loginResult.token)
                authTokenManager.saveIsLoginState(true)
                authTokenManager.saveUsername(result.loginResult.name)
                LoginUiState.Success(result)

            } catch (e: Exception) {
                LoginUiState.Error(e.message.toString())
            }
        }
    }
}