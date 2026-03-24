package com.khater.rwaq.presentation.screens.resetPasswordScreen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.khater.rwaq.presentation.navigation.Screen

fun NavGraphBuilder.resetPasswordScreenRoute(){
    composable<Screen.ResetPasswordScreen>{
        ResetPasswordScreen()
    }
}