package com.khater.rwaq.presentation.screens.splashScreen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.khater.rwaq.presentation.navigation.Screen

fun NavGraphBuilder.splashScreenRoute(){
    composable<Screen.SplashScreen>{
        SplashScreen()
    }
}