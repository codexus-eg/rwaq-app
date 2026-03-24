package com.khater.rwaq.presentation.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.khater.rwaq.presentation.util.LocalNavigationProvider
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun <T> EventHandler(
    effects: SharedFlow<T>,
    handleEffect: (T, NavController) -> Unit,
) {
    val navController = LocalNavigationProvider.current
    LaunchedEffect(key1 = Unit) {
        effects.collectLatest { effect ->
            handleEffect(effect,navController)
        }
    }
}