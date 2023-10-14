package com.example.mystoryappcompose.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.mystoryappcompose.data.network.response.ListStoryItem
import com.example.mystoryappcompose.ui.common.HomeUiState
import com.example.mystoryappcompose.ui.component.LoadingScreen
import com.example.mystoryappcompose.ui.component.MToast
import com.example.mystoryappcompose.ui.component.StoryItem

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    ) {

    val context = LocalContext.current
    when (uiState) {
        is HomeUiState.Loading -> LoadingScreen()
        is HomeUiState.Success -> {
            HomeScreenContent(uiState.getStoriesResponse.listStory)
        }
        is HomeUiState.Error -> MToast(context, uiState.msg)
    }
}

@Composable
fun HomeScreenContent(
    storyList: List<ListStoryItem>

) {
    StoryList(storyList = storyList)
}

@Composable
fun StoryList(
    storyList: List<ListStoryItem>,
    modifier: Modifier = Modifier
) {
    LazyColumn() {
        items(storyList) { data ->
            StoryItem(story = data, modifier = modifier.clickable { })
        }
    }
}