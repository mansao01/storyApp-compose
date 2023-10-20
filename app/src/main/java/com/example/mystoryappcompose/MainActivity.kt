package com.example.mystoryappcompose

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.mystoryappcompose.preferences.AuthViewModel
import com.example.mystoryappcompose.ui.MyStoryApp
import com.example.mystoryappcompose.ui.theme.MyStoryAppComposeTheme

class MainActivity : ComponentActivity() {
    private val authViewModel by viewModels<AuthViewModel> { AuthViewModel.Factory }
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            installSplashScreen().setKeepOnScreenCondition { !authViewModel.isLoading.value }
            MyStoryAppComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val startDestination = authViewModel.startDestination

                    MyStoryApp(startDestination = startDestination.value)
                }
            }
        }
    }
}
