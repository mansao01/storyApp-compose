package com.example.mystoryappcompose.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyStoryApp(
    navController: NavHostController = rememberNavController(),
    startDestination: String,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    NavHost(navController = navController, startDestination = startDestination) {

        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory)
            LoginScreen(
                uiState = loginViewModel.uiState,
                navigateToHome = {
                    navController.navigate(Screen.Home.route)
                    navController.popBackStack()
                },
                navigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Home.route) {
            val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
            HomeScreen(
                uiState = homeViewModel.uiState,
                scrollBehavior = scrollBehavior,
                navigateToLogin = {
                    navController.navigate(Screen.Login.route)
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Register.route) {
            val registerViewModel: RegisterViewModel =
                viewModel(factory = RegisterViewModel.Factory)
            RegisterScreen(uiState = registerViewModel.uiState, navigateToLogin = {
                navController.navigate(Screen.Login.route)
            })
        }

    }

}