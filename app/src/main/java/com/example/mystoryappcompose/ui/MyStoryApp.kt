package com.example.mystoryappcompose.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mystoryappcompose.ui.screen.login.LoginScreen
import com.example.mystoryappcompose.ui.screen.login.LoginViewModel

@Composable
fun MyStoryApp() {
    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory)
    LoginScreen(uiState = loginViewModel.uiState)
}