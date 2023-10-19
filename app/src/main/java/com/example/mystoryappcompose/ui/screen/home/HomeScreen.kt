package com.example.mystoryappcompose.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import com.example.mystoryappcompose.data.network.response.ListStoryItem
import com.example.mystoryappcompose.preferences.AuthViewModel
import com.example.mystoryappcompose.ui.SharedViewModel
import com.example.mystoryappcompose.ui.common.HomeUiState
import com.example.mystoryappcompose.ui.component.LoadingScreen
import com.example.mystoryappcompose.ui.component.MToast
import com.example.mystoryappcompose.ui.component.MyFloatingActionButton
import com.example.mystoryappcompose.ui.component.StoryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory),
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory),
    navigateToLogin: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    navigateToAdd: () -> Unit,
    sharedViewModel: SharedViewModel,
    navigateToDetail: () -> Unit

) {
    val isLogin by authViewModel.loginState.collectAsState()
    if (isLogin) {
        LaunchedEffect(Unit) {
            homeViewModel.getStories()
        }
    }

    val context = LocalContext.current
    when (uiState) {
        is HomeUiState.Loading -> LoadingScreen()
        is HomeUiState.Success -> {
            HomeScreenContent(
                uiState.getStoriesResponse.listStory,
                scrollBehavior = scrollBehavior,
                navigateToLogin = navigateToLogin,
                homeViewModel = homeViewModel,
                navigateToAdd = navigateToAdd,
                navigateToDetail = navigateToDetail,
                sharedViewModel = sharedViewModel,
                username = uiState.username
            )
        }

        is HomeUiState.Error -> MToast(context, uiState.msg)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    storyList: List<ListStoryItem>,
    scrollBehavior: TopAppBarScrollBehavior,
    navigateToLogin: () -> Unit,
    homeViewModel: HomeViewModel,
    navigateToAdd: () -> Unit,
    navigateToDetail: () -> Unit,
    sharedViewModel: SharedViewModel,
    username:String
) {
    Scaffold(
        topBar = {
            HomeTopBar(
                scrollBehavior = scrollBehavior,
                navigateToLogin = { navigateToLogin() },
                homeViewModel = homeViewModel,
                username = username
            )
        },
        floatingActionButton = { MyFloatingActionButton(navigateToAdd = { navigateToAdd() }) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        Surface(modifier = Modifier.padding(it)) {
            StoryList(
                storyList = storyList,
                sharedViewModel = sharedViewModel,
                navigateToDetail = navigateToDetail
            )
        }

    }
}

@Composable
fun StoryList(
    storyList: List<ListStoryItem>,
    modifier: Modifier = Modifier,
    sharedViewModel: SharedViewModel,
    navigateToDetail: () -> Unit
) {
    LazyColumn {
        items(storyList) { data ->
            StoryItem(story = data, modifier = modifier.clickable {
                sharedViewModel.addStory(newStory = data)
                navigateToDetail()
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
@Composable
fun HomeTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    navigateToLogin: () -> Unit,
    homeViewModel: HomeViewModel,
    username: String
) {
    val context = LocalContext.current
    LargeTopAppBar(
        scrollBehavior = scrollBehavior,
        title = { Text(text = "Welcome, $username") },
        actions = {
            IconButton(onClick = {
                context.imageLoader.memoryCache?.clear()
                context.imageLoader.diskCache?.clear()
                logout(homeViewModel, navigateToLogin)
            }) {
                Icon(imageVector = Icons.Default.Logout, contentDescription = "Logout")
            }
        }
    )
}

private fun logout(homeViewModel: HomeViewModel, navigateToLogin: () -> Unit) {
    homeViewModel.logout()
    navigateToLogin()


}