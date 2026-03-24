package com.khater.rwaq.presentation.screens.onBoardingScreen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.khater.rwaq.presentation.navigation.Screen

fun NavGraphBuilder.onBoardingScreenRoute(){
    composable<Screen.OnBoardingScreen>{
        OnBoardingScreen()
    }
}