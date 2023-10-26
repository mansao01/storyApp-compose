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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val myStoryRepository: MyStoryRepository,
    private val authTokenManager: AuthTokenManager
) : ViewModel() {
    var uiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    private val _isLoading  = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

      fun getStories(){
        viewModelScope.launch {
            _isLoading.value = true
            uiState = HomeUiState.Loading
            uiState = try {
                val result = myStoryRepository.getStories()
                val username = authTokenManager.getUsername()
                _isLoading.value = false
                HomeUiState.Success(result, username!!)
            }catch (e:Exception){
                _isLoading.value = false
                HomeUiState.Error(e.toString())
            }
        }
    }

    fun logout(){
        viewModelScope.launch {
            uiState = HomeUiState.Loading
            authTokenManager.clearTokens()
            authTokenManager.saveIsLoginState(false)
            authTokenManager.clearUsername()
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