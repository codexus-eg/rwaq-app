package com.khater.rwaq.presentation.screens.splashScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.khater.rwaq.designSystem.component.indicator.DotsProgressIndicator
import com.khater.rwaq.designSystem.component.text.Text
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.composables.EventHandler
import com.khater.rwaq.presentation.navigation.Screen
import com.khater.rwaq.presentation.screens.splashScreen.composables.LogoWithBlurEffect
import com.khater.rwaq.presentation.screens.splashScreen.uiState.SplashUiEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashScreen(
    splashScreenViewModel: SplashScreenViewModel = koinViewModel(),
) {

    val state = splashScreenViewModel.state.collectAsStateWithLifecycle().value
    EventHandler(splashScreenViewModel.effect) { effect, navController ->

        when (effect) {
            SplashUiEffect.NavigateToHome -> {
                navController.navigate(Screen.HomeScreen) {
                    popUpTo(0)
                }
            }

            SplashUiEffect.NavigateToOnBoarding -> {
                navController.navigate(Screen.LoginScreen) {
                    popUpTo(0)
                }
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colorScheme.background.surface),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier.fillMaxSize().padding(bottom = 40.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = state.startAnimation,
                enter = slideInVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                ),
                modifier = Modifier.weight(1f)
            ) {
                LogoWithBlurEffect()
            }
            DotsProgressIndicator(dotSize = 8.dp)
        }

    }
}
