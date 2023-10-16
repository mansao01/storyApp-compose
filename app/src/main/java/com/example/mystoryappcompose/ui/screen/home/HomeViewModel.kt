package com.example.mystoryappcompose.ui.screen.home

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
import com.example.mystoryappcompose.ui.common.HomeUiState
import kotlinx.coroutines.launch

class HomeViewModel(
    private val myStoryRepository: MyStoryRepository,
    private val authTokenManager: AuthTokenManager
) : ViewModel() {
    var uiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set



      fun getStories(){
        viewModelScope.launch {
            uiState = HomeUiState.Loading
            uiState = try {
                val result = myStoryRepository.getStories("Bearer ${authTokenManager.getAccessToken()}")
                HomeUiState.Success(result)
            }catch (e:Exception){

                HomeUiState.Error(e.toString())
            }
        }
    }

    fun logout(){
        viewModelScope.launch {
            uiState = HomeUiState.Loading
            authTokenManager.clearTokens()
            authTokenManager.saveIsLoginState(false)
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyStoryApplication)
                val noteRepository = application.container.myStoryRepository
                val authTokenManager = application.authTokenManager
                HomeViewModel(
                    myStoryRepository = noteRepository,
                    authTokenManager = authTokenManager
                )
            }
        }
    }
}