package com.example.mystoryappcompose.ui.screen.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.mystoryappcompose.data.network.response.ListStoryWitLocationItem
import com.example.mystoryappcompose.ui.common.MapUiState
import com.example.mystoryappcompose.ui.component.LoadingScreen
import com.example.mystoryappcompose.ui.component.MToast
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun MapScreen(
    uiState: MapUiState
) {
    val context = LocalContext.current
    when (uiState) {
        is MapUiState.Loading -> LoadingScreen()
        is MapUiState.Success -> MapScreenContent(storyList = uiState.getStoriesWithLocationResponse.listStoryWithLocation)
        is MapUiState.Error -> MToast(context = context, message = uiState.msg)
    }
}

@Composable
fun MapScreenContent(
    storyList: List<ListStoryWitLocationItem>
) {

    GoogleMap{
        storyList.forEach { storyItem ->
            val lat = storyItem.lat
            val lon = storyItem.lon
            val position = LatLng(lat, lon)
            Marker(
                state = MarkerState(position),
                title = storyItem.name,
                snippet = storyItem.description
            )
        }

    }
}