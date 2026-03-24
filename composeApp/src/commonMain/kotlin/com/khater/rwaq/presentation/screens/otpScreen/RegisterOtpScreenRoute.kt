package com.khater.rwaq.presentation.screens.otpScreen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.khater.rwaq.presentation.navigation.Screen

fun NavGraphBuilder.registerOtpScreenRoute(){
    composable<Screen.OTPScreen> {
        RegisterOtpScreen()
    }
}