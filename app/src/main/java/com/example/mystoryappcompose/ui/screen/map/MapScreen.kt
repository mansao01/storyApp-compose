package com.example.mystoryappcompose.ui.screen.map

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.mystoryappcompose.R
import com.example.mystoryappcompose.data.local.model.LocationModel
import com.example.mystoryappcompose.data.network.response.ListStoryWitLocationItem
import com.example.mystoryappcompose.ui.common.MapUiState
import com.example.mystoryappcompose.ui.component.LoadingScreen
import com.example.mystoryappcompose.ui.component.MToast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(
    uiState: MapUiState,
    location: LocationModel,
    locationEnabled: Boolean,
    navigateToHome: () -> Unit
) {
    val context = LocalContext.current
    Scaffold(topBar = { MapTopBar(navigateToHome = { navigateToHome() }) }) {
        Surface(modifier = Modifier.padding(it)) {
            when (uiState) {
                is MapUiState.Loading -> LoadingScreen()
                is MapUiState.Success -> MapScreenContent(
                    storyList = uiState.getStoriesWithLocationResponse.listStoryWithLocation,
                    location = location,
                    locationEnabled = locationEnabled
                )

                is MapUiState.Error -> MToast(context = context, message = uiState.msg)
            }
        }
    }
}

@Composable
fun MapScreenContent(
    storyList: List<ListStoryWitLocationItem>,
    location: LocationModel,
    locationEnabled: Boolean
) {
    Log.d("Location", location.toString())
    val boundsBuilder = LatLngBounds.builder()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(location.latitude, location.longitude), 10f)
    }
    GoogleMap(
        properties = MapProperties(isMyLocationEnabled = locationEnabled),
        cameraPositionState = cameraPositionState
    ) {
        storyList.forEach { storyItem ->
            val lat = storyItem.lat
            val lon = storyItem.lon
            val position = LatLng(lat, lon)
            boundsBuilder.include(position)
            Marker(
                state = MarkerState(position),
                title = storyItem.name,
                snippet = storyItem.description,
            )
            cameraPositionState.move(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 64))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapTopBar(
    navigateToHome: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.story_location)) },
        navigationIcon = {
            IconButton(onClick = { navigateToHome() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        }
    )
}