package com.khater.rwaq.presentation.screens.loginScreen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.khater.rwaq.presentation.navigation.Screen

fun NavGraphBuilder.loginScreenRoute(){
    composable<Screen.LoginScreen>{
        LoginScreen()
    }
}