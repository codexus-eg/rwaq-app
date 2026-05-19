package com.khater.rwaq.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.khater.rwaq.designSystem.theme.theme.RwaqTheme
import com.khater.rwaq.domain.repository.authentication.AuthenticationRepository
import com.khater.rwaq.domain.repository.service.LocalizationService
import com.khater.rwaq.domain.useCases.cart.ObserveCartBadgeCountUseCase
import com.khater.rwaq.presentation.composables.AppUpdateBottomSheet
import com.khater.rwaq.presentation.composables.RwaqBottomNavigation
import com.khater.rwaq.presentation.navigation.NavigationBarItems
import com.khater.rwaq.presentation.navigation.RwaqNavGraph
import com.khater.rwaq.presentation.navigation.Screen
import com.khater.rwaq.presentation.util.LocalNavigationProvider
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
@OptIn(ExperimentalComposeUiApi::class)
fun App(
    appViewModel: AppViewModel = koinViewModel()
) {
    val localizationService = koinInject<LocalizationService>()
    val currentLanguage by localizationService.observeLanguage().collectAsStateWithLifecycle()
    val appState by appViewModel.state.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current

    val authenticationRepository = koinInject<AuthenticationRepository>()
    val observeCartBadgeCountUseCase = koinInject<ObserveCartBadgeCountUseCase>()
    val isUserLoggedIn by authenticationRepository.isUserLoggedIn().collectAsStateWithLifecycle(initialValue = false)
    val cartBadgeCount by observeCartBadgeCountUseCase().collectAsStateWithLifecycle(initialValue = 0)

    LaunchedEffect(appViewModel.effect) {
        appViewModel.effect.collectLatest { effect ->
            when (effect) {
                is AppUiEffect.OpenAppUpdateUrl -> {
                    runCatching { uriHandler.openUri(effect.url) }
                }
            }
        }
    }

     RwaqTheme(
        language = currentLanguage.iso
    ) {
        val navController = rememberNavController()
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination: NavDestination? = backStackEntry?.destination
        var selectedScreen by remember { mutableIntStateOf(0) }

        var isVisible by rememberSaveable { mutableStateOf(false) }
        LaunchedEffect(currentDestination) {
            currentDestination?.let { destination ->
                val currentIndex = NavigationBarItems.entries.indexOfFirst { item ->
                    destination.hierarchy.any {
                        it.hasRoute(item.route::class)
                    }
                }
                if (currentIndex != -1) {
                    selectedScreen = currentIndex
                }
            }

            isVisible = NavigationBarItems.entries.map { it.route::class }.any { route ->
                currentDestination?.hierarchy?.any {
                    it.hasRoute(route)
                } == true
            }
        }

        BackHandler(enabled = appState.appUpdateSheet != null) {
            appViewModel.onDismissAppUpdate()
        }

        CompositionLocalProvider(LocalNavigationProvider provides navController) {
            Box(modifier = Modifier.fillMaxSize()) {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    containerColor = Color.White,
                    bottomBar = {
                        AnimatedVisibility(
                            visible = isVisible,
                            enter = slideInVertically(animationSpec = tween(600)) { it },
                            exit = slideOutVertically(animationSpec = tween(600)) { 2 * it }) {
                            RwaqBottomNavigation(
                                screens = NavigationBarItems.entries,
                                selectedScreen = selectedScreen,
                                badgeCount = if (isUserLoggedIn) cartBadgeCount else 0
                            ) { clickedIndex ->
                                if (selectedScreen != clickedIndex) {
                                    selectedScreen = clickedIndex
                                    val selectedItem = NavigationBarItems.entries[clickedIndex]
                                    navController.navigate(selectedItem.route) {
                                        popUpTo(
                                            Screen.HomeScreen
                                        ) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        }
                    }
                ) {
                    RwaqNavGraph(modifier = Modifier)
                }

                AppUpdateBottomSheet(
                    isVisible = appState.appUpdateSheet != null,
                    update = appState.appUpdateSheet,
                    onUpdateClick = appViewModel::onUpdateAppClicked,
                    onLaterClick = appViewModel::onDismissAppUpdate
                )
            }
        }
    }
}
