package com.mansao.mystoryappcompose.ui.screen.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.mystoryappcompose.data.MyStoryRepository
import com.mansao.mystoryappcompose.preferences.AuthTokenManager
import com.mansao.mystoryappcompose.ui.common.HomeUiState
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
}