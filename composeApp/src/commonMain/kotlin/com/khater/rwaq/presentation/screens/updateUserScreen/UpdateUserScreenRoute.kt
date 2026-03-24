package com.khater.rwaq.presentation.screens.updateUserScreen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.khater.rwaq.presentation.navigation.Screen
import com.khater.rwaq.presentation.screens.branchScreen.BranchScreen
import com.khater.rwaq.presentation.screens.branchesScreen.BranchesScreen
import com.khater.rwaq.presentation.screens.contactUsScreen.ContactUsScreen
import com.khater.rwaq.presentation.screens.homeScreen.HomeScreen
import com.khater.rwaq.presentation.screens.splashScreen.SplashScreen

fun NavGraphBuilder.updateUserScreenRoute(){
    composable<Screen.UpdateUserScreen>{
        UpdateUserScreen()
    }
}