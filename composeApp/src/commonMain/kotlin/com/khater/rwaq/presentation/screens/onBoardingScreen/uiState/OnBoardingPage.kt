package com.khater.rwaq.presentation.screens.onBoardingScreen.uiState

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

// 1. Data Model for the content
data class OnBoardingPage(
    val imageRes:  DrawableResource,
    val titleRes: StringResource,
    val descRes: StringResource
)