package com.khater.rwaq.presentation.screens.splashScreen.uiState

import com.khater.rwaq.presentation.model.SnackBarState


data class SplashUiState(
    val startAnimation: Boolean = false,
    val animationFinished: Boolean = false,
    val authFinished: Boolean = false,
    val isLoggedIn: Boolean? = null,
    val snackBar: SnackBarState = SnackBarState(),
    )
