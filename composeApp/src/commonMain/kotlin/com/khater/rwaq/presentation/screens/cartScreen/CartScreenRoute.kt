package com.khater.rwaq.presentation.screens.cartScreen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.khater.rwaq.presentation.navigation.Screen

fun NavGraphBuilder.cartScreenRoute(){
    composable<Screen.CartScreen>{
        CartScreen()
    }
}