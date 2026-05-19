package com.khater.rwaq.presentation.screens.homeScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.khater.rwaq.designSystem.component.indicator.DotsProgressIndicator
import com.khater.rwaq.designSystem.component.scaffold.HomeScaffold
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.composables.EmptyOrErrorContent
import com.khater.rwaq.presentation.composables.EventHandler
import com.khater.rwaq.presentation.navigation.Screen.BranchScreen
import com.khater.rwaq.presentation.screens.homeScreen.components.CoffeeContent
import com.khater.rwaq.presentation.screens.homeScreen.uiStates.HomeScreenInteractionListener
import com.khater.rwaq.presentation.screens.homeScreen.uiStates.HomeUiEffect
import com.khater.rwaq.presentation.screens.homeScreen.uiStates.HomeUiState
import com.khater.rwaq.presentation.screens.productScreen.components.ProductDetailsSheet
import com.khater.rwaq.presentation.screens.productScreen.components.SearchBottomSheet
import com.khater.rwaq.presentation.util.Dimensions.BOTTOM_NAV_HEIGHT
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.empty_product
import rwaq.composeapp.generated.resources.empty_products
import rwaq.composeapp.generated.resources.something_went_wrong

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel = koinViewModel()) {

    val state = homeViewModel.state.collectAsStateWithLifecycle().value
    val gridState = rememberLazyGridState()
    val scope = rememberCoroutineScope()

    EventHandler(homeViewModel.effect) { effect, controller ->
        when (effect) {
            HomeUiEffect.NavigateToPickUpFromBranch -> {
                controller.navigate(BranchScreen(isPickupFormBranch = true))
            }

            HomeUiEffect.NavigateToPickUpFromCar -> {
                controller.navigate(BranchScreen(isPickupFormBranch = false))
            }
            is HomeUiEffect.ScrollToCategory -> {
                scope.launch {
                    // Enhanced smooth scroll with spring animation
                    delay(50) // Small delay for better visual feedback

                    gridState.animateScrollToItem(
                        index = effect.index
                    )
                }
            }
            is HomeUiEffect.NavigateToProductDetails -> {}
        }

    }
    BackHandler(enabled = state.isDetailsVisible || state.isSearchVisible) {
        when {
            state.isDetailsVisible -> homeViewModel.onDismissDetails()
            state.isSearchVisible -> homeViewModel.onDismissSearch()
        }
    }

    HomeContent(
        state = state,
        interactionListener = homeViewModel as HomeScreenInteractionListener,
        gridState = gridState
    )

}

@Composable
fun HomeContent(
    state: HomeUiState,
    interactionListener: HomeScreenInteractionListener,
    gridState : LazyGridState
) {
    Box(modifier = Modifier.fillMaxSize()) {

        HomeScaffold(
            snackBarState = state.snackBar,
            hasStatusBarColor = false,
        ) {

            Column(
                modifier = Modifier.fillMaxSize().padding(bottom = BOTTOM_NAV_HEIGHT.dp),
                 verticalArrangement = Arrangement.spacedBy(
                    Theme.spacing._12,
                 )
            ) {
                  when{
                    state.isLoading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                            DotsProgressIndicator(dotSize = 8.dp)

                        }
                    }
                    state.errorMessage != null -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){

                        EmptyOrErrorContent(
                            message = state.errorMessage,
                            painter = painterResource(Res.drawable.something_went_wrong),
                            isError = true,
                            onRetry = interactionListener::onRetry
                        )}
                    }
                    state.products.isEmpty() && !state.isLoading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){

                        EmptyOrErrorContent(
                                message = stringResource(Res.string.empty_products),
                            painter = painterResource(Res.drawable.empty_product),
                         )
                    }}
                    else ->{

                        CoffeeContent(state = state, listener = interactionListener,gridState)
                    }
                }

            }
        }


        SearchBottomSheet(
            isVisible = state.isSearchVisible,
            searchQuery = state.searchQuery,
            filteredProducts = state.filteredProducts,
            onDismiss = interactionListener::onDismissSearch,
            onSearchQueryChange = interactionListener::onSearchQueryChange,
            onProductClick = interactionListener::onProductClicked
        )

        ProductDetailsSheet(
            isVisible = state.isDetailsVisible,
            details = state.selectedProductDetails,
            onDismiss = interactionListener::onDismissDetails,
            listener = interactionListener
        )

    }
}
