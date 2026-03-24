package com.khater.rwaq.presentation.screens.branchScreen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.khater.rwaq.presentation.navigation.Screen
import com.khater.rwaq.presentation.screens.homeScreen.HomeScreen
import com.khater.rwaq.presentation.screens.splashScreen.SplashScreen

fun NavGraphBuilder.branchScreenRoute(){
    composable<Screen.BranchScreen>{
        BranchScreen()
    }
}