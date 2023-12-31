package com.mansao.mystoryappcompose.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Add : Screen("add")
    object Home : Screen("home")
    object Detail : Screen("home/detail")
    object Map : Screen("home/map")


}