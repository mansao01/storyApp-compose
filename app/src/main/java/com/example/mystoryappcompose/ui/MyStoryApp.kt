package com.example.mystoryappcompose.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mystoryappcompose.ui.navigation.Screen
import com.example.mystoryappcompose.ui.screen.home.HomeScreen
import com.example.mystoryappcompose.ui.screen.home.HomeViewModel
import com.example.mystoryappcompose.ui.screen.login.LoginScreen
import com.example.mystoryappcompose.ui.screen.login.LoginViewModel
import com.example.mystoryappcompose.ui.screen.regsiter.RegisterScreen
import com.example.mystoryappcompose.ui.screen.regsiter.RegisterViewModel

@Composable
fun MyStoryApp(
    navController: NavHostController = rememberNavController(),

    ) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {

        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory)
            LoginScreen(
                uiState = loginViewModel.uiState,
                navigateToHome = { navController.navigate(Screen.Home.route) })
        }

        composable(Screen.Home.route) {
            val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
            HomeScreen(uiState = homeViewModel.uiState)
        }

        composable(Screen.Register.route) {
            val registerViewModel: RegisterViewModel =
                viewModel(factory = RegisterViewModel.Factory)
            RegisterScreen(uiState = registerViewModel.uiState)
        }

    }

}