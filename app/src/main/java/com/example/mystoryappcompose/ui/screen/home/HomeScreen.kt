package com.example.mystoryappcompose.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mystoryappcompose.data.network.response.ListStoryItem
import com.example.mystoryappcompose.preferences.AuthViewModel
import com.example.mystoryappcompose.ui.common.HomeUiState
import com.example.mystoryappcompose.ui.component.LoadingScreen
import com.example.mystoryappcompose.ui.component.MToast
import com.example.mystoryappcompose.ui.component.StoryItem

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory),
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)
) {
    val isLogin by authViewModel.loginState.collectAsState()
    if (isLogin){
        LaunchedEffect(Unit){
            homeViewModel.getStories()
        }
    }


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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    navigateToLogin: () -> Unit,
    homeViewModel: HomeViewModel,
) {
    LargeTopAppBar(
        scrollBehavior = scrollBehavior,
        title = { Text(text = "Welcome") },
        actions = {
            IconButton(onClick = {
                navigateToLogin()
                homeViewModel.logout()
            }) {
                Icon(imageVector = Icons.Default.Logout, contentDescription = "Logout")
            }
        }
    )
}