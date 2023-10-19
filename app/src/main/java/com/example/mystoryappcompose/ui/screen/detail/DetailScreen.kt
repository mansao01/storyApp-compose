package com.example.mystoryappcompose.ui.screen.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mystoryappcompose.ui.SharedViewModel

@Composable
fun DetailScreen(
    sharedViewModel: SharedViewModel
) {
    val context = LocalContext.current
    val story = sharedViewModel.storyItem
    Column {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(story?.photoUrl)
                .crossfade(true)
                .build(),
            contentDescription = "story photo",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        story?.name?.let { Text(text = it) }
        Spacer(modifier = Modifier.height(32.dp))
        story?.description?.let { Text(text = it) }
    }
}