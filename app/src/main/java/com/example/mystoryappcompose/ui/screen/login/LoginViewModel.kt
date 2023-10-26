package com.example.mystoryappcompose.ui.screen.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mystoryappcompose.MyStoryApplication
import com.example.mystoryappcompose.data.MyStoryRepository
import com.example.mystoryappcompose.preferences.AuthTokenManager
import com.example.mystoryappcompose.ui.common.LoginUiState
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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyStoryApplication)
                val noteRepository = application.container.myStoryRepository
                val authTokenManager = application.authTokenManager
                LoginViewModel(
                    myStoryRepository = noteRepository,
                    authTokenManager = authTokenManager
                )
            }
        }
    }
}