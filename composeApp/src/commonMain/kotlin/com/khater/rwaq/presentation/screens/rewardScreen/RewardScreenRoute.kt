package com.khater.rwaq.presentation.screens.rewardScreen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.khater.rwaq.presentation.navigation.Screen
import com.khater.rwaq.presentation.screens.homeScreen.HomeScreen
import com.khater.rwaq.presentation.screens.splashScreen.SplashScreen

fun NavGraphBuilder.rewardScreenRoute(){
    composable<Screen.RewardScreen>{
        NewRewardScreen()
    }
}