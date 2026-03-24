package com.khater.rwaq.presentation.screens.registerScreen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.khater.rwaq.presentation.navigation.Screen

fun NavGraphBuilder.registerScreenRoute(){
    composable<Screen.RegisterScreen>{
        RegisterScreen()
    }
}