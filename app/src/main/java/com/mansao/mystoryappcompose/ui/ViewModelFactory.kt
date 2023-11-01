package com.mansao.mystoryappcompose.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mansao.mystoryappcompose.MyStoryApplication
import com.mansao.mystoryappcompose.preferences.AuthViewModel
import com.mansao.mystoryappcompose.ui.screen.add.AddViewModel
import com.mansao.mystoryappcompose.ui.screen.home.HomeViewModel
import com.mansao.mystoryappcompose.ui.screen.login.LoginViewModel
import com.mansao.mystoryappcompose.ui.screen.map.MapViewModel
import com.mansao.mystoryappcompose.ui.screen.regsiter.RegisterViewModel

object ViewModelFactory {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                myStoryRepository = myStoryApplication().container.myStoryRepository,
                authTokenManager = myStoryApplication().authTokenManager
            )
        }

        initializer {
            AddViewModel(
                myStoryRepository = myStoryApplication().container.myStoryRepository,
                )
        }

        initializer {
            LoginViewModel(
                myStoryRepository = myStoryApplication().container.myStoryRepository,
                authTokenManager = myStoryApplication().authTokenManager
            )
        }

        initializer {
            RegisterViewModel(
                myStoryRepository = myStoryApplication().container.myStoryRepository,
            )
        }
        initializer {
            MapViewModel(
                myStoryRepository = myStoryApplication().container.myStoryRepository,
                )
        }
        initializer {
            AuthViewModel(
                authTokenManager = myStoryApplication().authTokenManager
            )
        }
    }
}

fun CreationExtras.myStoryApplication(): MyStoryApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyStoryApplication)