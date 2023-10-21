package com.example.mystoryappcompose.ui.screen.map

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.mystoryappcompose.data.model.LocationModel
import com.example.mystoryappcompose.data.network.response.ListStoryWitLocationItem
import com.example.mystoryappcompose.ui.common.MapUiState
import com.example.mystoryappcompose.ui.component.LoadingScreen
import com.example.mystoryappcompose.ui.component.MToast
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun MapScreen(
    uiState: MapUiState,
    location: LocationModel
) {
    val context = LocalContext.current
    when (uiState) {
        is MapUiState.Loading -> LoadingScreen()
        is MapUiState.Success -> MapScreenContent(
            storyList = uiState.getStoriesWithLocationResponse.listStoryWithLocation,
            location = location
        )

        is MapUiState.Error -> MToast(context = context, message = uiState.msg)
    }
}

@Composable
fun MapScreenContent(
    storyList: List<ListStoryWitLocationItem>,
    location: LocationModel

) {
    Log.d("Location", location.toString())
    val boundsBuilder = LatLngBounds.builder()
    GoogleMap(
        properties = MapProperties(isMyLocationEnabled = true)
    ) {
        storyList.forEach { storyItem ->
            val lat = storyItem.lat
            val lon = storyItem.lon
            val position = LatLng(lat, lon)
            boundsBuilder.include(position)
            Marker(
                state = MarkerState(position),
                title = storyItem.name,
                snippet = storyItem.description
            )
        }
    }
}

private fun hasLocationPermission(context: Context):Boolean {
    return ContextCompat.checkSelfPermission(
        context, android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}