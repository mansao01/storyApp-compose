package com.mansao.mystoryappcompose.ui.screen.regsiter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mansao.mystoryappcompose.MyStoryApplication
import com.mansao.mystoryappcompose.data.MyStoryRepository
import com.mansao.mystoryappcompose.ui.common.RegisterUiState
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class RegisterViewModel(
    private val myStoryRepository: MyStoryRepository
):ViewModel() {

    var uiState: RegisterUiState by mutableStateOf(RegisterUiState.StandBy)
        private set

    fun getUiState() {
        uiState = RegisterUiState.StandBy
    }

    fun register(name:String, email:String, password:String){
        viewModelScope.launch {
            uiState = RegisterUiState.Loading
            uiState = try {
                val  result = myStoryRepository.register(name, email, password)
                RegisterUiState.Success(result)
            }catch (e:Exception){
                val errorMessage = when (e) {
                    is IOException -> "Network error occurred"
                    is HttpException -> {
                        when (e.code()) {
                            400 -> e.response()?.errorBody()?.string().toString()
                            // Add more cases for specific HTTP error codes if needed
                            else -> "HTTP error: ${e.code()}"
                        }
                    }

                    else -> "An unexpected error occurred"
                }
                RegisterUiState.Error(errorMessage)
            }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyStoryApplication)
                val noteRepository = application.container.myStoryRepository
                RegisterViewModel(myStoryRepository = noteRepository)
            }
        }
    }
}