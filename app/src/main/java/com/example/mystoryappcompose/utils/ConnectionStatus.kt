package com.example.mystoryappcompose.utils

sealed class ConnectionStatus{
    object  Available:ConnectionStatus()
    object  UnAvailable:ConnectionStatus()
}