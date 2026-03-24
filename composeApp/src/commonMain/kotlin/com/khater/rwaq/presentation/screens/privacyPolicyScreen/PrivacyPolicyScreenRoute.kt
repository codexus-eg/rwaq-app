package com.khater.rwaq.presentation.screens.privacyPolicyScreen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.khater.rwaq.presentation.navigation.Screen
import com.khater.rwaq.presentation.screens.onBoardingScreen.OnBoardingScreen

fun NavGraphBuilder.privacyPolicyScreenRoute(){
    composable<Screen.PrivacyPolicyScreen>{
        PrivacyPolicyScreen()
    }
}