package com.khater.rwaq.presentation.screens.forgetPasswordScreen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.khater.rwaq.presentation.navigation.Screen
import com.khater.rwaq.presentation.screens.forgetPasswordScreen.ForgetPasswordScreen

fun NavGraphBuilder.forgetPasswordScreenRoute(){
    composable<Screen.ForgetPasswordScreen>{
        ForgetPasswordScreen()
    }
}