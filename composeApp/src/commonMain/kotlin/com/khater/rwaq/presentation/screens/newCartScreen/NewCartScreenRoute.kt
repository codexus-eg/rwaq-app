package com.khater.rwaq.presentation.screens.newCartScreen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.khater.rwaq.presentation.navigation.Screen

fun NavGraphBuilder.newCartScreenRoute() {
    composable<Screen.NewCartScreen> {
        NewCartScreen()
    }
}
