package com.khater.rwaq.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.khater.rwaq.designSystem.theme.theme.RwaqTheme
import com.khater.rwaq.domain.repository.service.LocalizationService
import com.khater.rwaq.domain.useCases.ManageCartUseCase
import com.khater.rwaq.presentation.composables.RwaqBottomNavigation
import com.khater.rwaq.presentation.navigation.NavigationBarItems
import com.khater.rwaq.presentation.navigation.RwaqNavGraph
import com.khater.rwaq.presentation.navigation.Screen
import com.khater.rwaq.presentation.util.LocalNavigationProvider
import com.swmansion.kmpmaps.core.CameraPosition
import com.swmansion.kmpmaps.core.Coordinates
import com.swmansion.kmpmaps.core.Map
import com.swmansion.kmpmaps.core.MapProperties
import com.swmansion.kmpmaps.core.MapType
import com.swmansion.kmpmaps.core.MapUISettings
import com.swmansion.kmpmaps.core.Marker
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    Map()
    val localizationService = koinInject<LocalizationService>()
    val currentLanguage by localizationService.observeLanguage().collectAsStateWithLifecycle()

    val manageCartUseCase = koinInject<ManageCartUseCase>()
    val cartOrders by manageCartUseCase().collectAsStateWithLifecycle(initialValue = emptyList())
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

        CompositionLocalProvider(LocalNavigationProvider provides navController) {
            val paddingValues = WindowInsets.navigationBars.asPaddingValues()

            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                ,
                containerColor = Color.White,
                bottomBar = {
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = slideInVertically(animationSpec = tween(600)) { it },
                        exit = slideOutVertically(animationSpec = tween(600)) { 2 * it }) {
                        RwaqBottomNavigation(
                            screens = NavigationBarItems.entries,
                            selectedScreen = selectedScreen,
                            badgeCount = cartOrders.size
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

        }
    }
}

