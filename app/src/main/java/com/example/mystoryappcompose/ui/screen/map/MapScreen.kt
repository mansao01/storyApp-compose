package com.example.mystoryappcompose.ui.screen.map

import android.util.Log
import androidx.compose.runtime.Composable
import com.google.maps.android.compose.GoogleMap

@Composable
fun MapScreen() {
    MapScreenContent()
    Log.d("Map", "MapScreen opened")
}

@Composable
fun MapScreenContent() {
    GoogleMap {

    }
}