package com.mansao.mystoryappcompose.ui.screen.add

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
import com.mansao.mystoryappcompose.ui.common.AddUiState
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

    fun postStory(file: File, description: String, lat: Float? = null, lon: Float? = null) {
        viewModelScope.launch {
            uiState = AddUiState.Loading
            uiState = try {
                val result = myStoryRepository.postStory(
                    file,
                    description,
                    lat,
                    lon
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