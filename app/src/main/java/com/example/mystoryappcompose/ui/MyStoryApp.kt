package com.example.mystoryappcompose.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mystoryappcompose.ui.screen.regsiter.RegisterScreen
import com.example.mystoryappcompose.ui.screen.regsiter.RegisterViewModel

@Composable
fun MyStoryApp() {
//    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory)
//    LoginScreen(uiState = loginViewModel.uiState)

    val registerViewModel:RegisterViewModel = viewModel(factory = RegisterViewModel.Factory)
    RegisterScreen(uiState = registerViewModel.uiState)
}