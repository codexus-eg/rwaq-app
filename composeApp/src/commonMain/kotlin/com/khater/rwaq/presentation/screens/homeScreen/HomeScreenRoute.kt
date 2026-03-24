package com.khater.rwaq.presentation.screens.homeScreen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.khater.rwaq.presentation.navigation.Screen
import com.khater.rwaq.presentation.screens.splashScreen.SplashScreen

fun NavGraphBuilder.homeScreenRoute(){
    composable<Screen.HomeScreen>{
        HomeScreen()
    }
}