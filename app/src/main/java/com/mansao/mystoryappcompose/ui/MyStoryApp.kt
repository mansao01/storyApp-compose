package com.mansao.mystoryappcompose.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mansao.mystoryappcompose.data.local.model.LocationModel
import com.mansao.mystoryappcompose.ui.navigation.Screen
import com.mansao.mystoryappcompose.ui.screen.add.AddScreen
import com.mansao.mystoryappcompose.ui.screen.add.AddViewModel
import com.mansao.mystoryappcompose.ui.screen.detail.DetailScreen
import com.mansao.mystoryappcompose.ui.screen.home.HomeScreen
import com.mansao.mystoryappcompose.ui.screen.home.HomeViewModel
import com.mansao.mystoryappcompose.ui.screen.login.LoginScreen
import com.mansao.mystoryappcompose.ui.screen.login.LoginViewModel
import com.mansao.mystoryappcompose.ui.screen.map.MapScreen
import com.mansao.mystoryappcompose.ui.screen.map.MapViewModel
import com.mansao.mystoryappcompose.ui.screen.regsiter.RegisterScreen
import com.mansao.mystoryappcompose.ui.screen.regsiter.RegisterViewModel

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyStoryApp(
    navController: NavHostController = rememberNavController(),
    startDestination: String,
    location: LocationModel,
    locationEnabled: Boolean
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val sharedViewModel: SharedViewModel = viewModel()
    NavHost(navController = navController, startDestination = startDestination) {

        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = viewModel(factory = ViewModelFactory.Factory)
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
            val homeViewModel: HomeViewModel = viewModel(factory = ViewModelFactory.Factory)
            HomeScreen(
                uiState = homeViewModel.uiState,
                scrollBehavior = scrollBehavior,
                navigateToLogin = {
                    navController.navigate(Screen.Login.route)
                    navController.popBackStack()
                },
                navigateToAdd = {
                    navController.navigate(Screen.Add.route)
                },
                sharedViewModel = sharedViewModel,
                navigateToDetail = {
                    navController.navigate(Screen.Detail.route)
                },
                navigateToMap = {
                    navController.navigate(Screen.Map.route)
                }
            )
        }

        composable(Screen.Register.route) {
            val registerViewModel: RegisterViewModel =
                viewModel(factory = ViewModelFactory.Factory)
            RegisterScreen(uiState = registerViewModel.uiState, navigateToLogin = {
                navController.navigate(Screen.Login.route)
            })
        }

        composable(Screen.Add.route) {
            val addViewModel: AddViewModel = viewModel(factory = ViewModelFactory.Factory)
            AddScreen(
                uiState = addViewModel.uiState,
                scrollBehavior = scrollBehavior,
                navigateToHome = {
                    navController.popBackStack()
                    navController.navigate(Screen.Home.route)
                },
                location = location
            )
        }

        composable(Screen.Detail.route) {
            DetailScreen(
                sharedViewModel = sharedViewModel,
                navigateToHome = { navController.navigate(Screen.Home.route) },
                scrollBehavior = scrollBehavior
            )
        }

        composable(Screen.Map.route) {
            val mapViewModel: MapViewModel = viewModel(factory = ViewModelFactory.Factory)
            MapScreen(
                uiState = mapViewModel.uiState,
                location = location,
                locationEnabled = locationEnabled,
                navigateToHome = {
                    navController.popBackStack()
                    navController.navigate(Screen.Home.route)
                }
            )
        }
    }

}