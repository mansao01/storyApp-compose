package com.example.mystoryappcompose.ui.screen.add

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
import com.example.mystoryappcompose.ui.common.AddUiState
import kotlinx.coroutines.launch
import java.io.File

class AddViewModel(
    private val myStoryRepository: MyStoryRepository,
) : ViewModel() {

    var uiState: AddUiState by mutableStateOf(AddUiState.StandBy)
        private set

    fun updateUiState() {
        uiState = AddUiState.StandBy
    }

    fun postStory(file: File, description: String) {
        viewModelScope.launch {
            uiState = AddUiState.Loading
            uiState = try {
                val result = myStoryRepository.postStory(
                    file,
                    description
                )
                AddUiState.Success(result)
            } catch (e: Exception) {
                AddUiState.Error(e.toString())
            }

        }

    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyStoryApplication)
                val noteRepository = application.container.myStoryRepository
                AddViewModel(
                    myStoryRepository = noteRepository,
                )
            }
        }
    }
}