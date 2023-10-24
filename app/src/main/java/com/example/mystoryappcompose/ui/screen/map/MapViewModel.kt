package com.example.mystoryappcompose.ui.screen.map

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
import com.example.mystoryappcompose.ui.common.MapUiState
import kotlinx.coroutines.launch

class MapViewModel(
    private val myStoryRepository: MyStoryRepository,
) : ViewModel() {

    var uiState: MapUiState by mutableStateOf(MapUiState.Loading)

    init {
        getStoriesWithLocation()
    }

    private fun getStoriesWithLocation() {
        viewModelScope.launch {
            uiState = MapUiState.Loading
            uiState = try {
                val result =
                    myStoryRepository.getStoriesWithLocation()
                MapUiState.Success(result)
            } catch (e: Exception) {
                MapUiState.Error(e.message.toString())
            }

        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyStoryApplication)
                val noteRepository = application.container.myStoryRepository
                MapViewModel(
                    myStoryRepository = noteRepository,
                )
            }
        }
    }
}