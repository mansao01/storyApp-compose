package com.mansao.mystoryappcompose.ui.screen.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.mystoryappcompose.data.MyStoryRepository
import com.mansao.mystoryappcompose.ui.common.MapUiState
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

}