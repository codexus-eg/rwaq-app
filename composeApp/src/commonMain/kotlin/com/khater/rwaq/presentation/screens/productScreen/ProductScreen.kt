package com.khater.rwaq.presentation.screens.productScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.khater.rwaq.designSystem.component.indicator.DotsProgressIndicator
import com.khater.rwaq.designSystem.component.scaffold.HomeScaffold
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.composables.EmptyOrErrorContent
import com.khater.rwaq.presentation.composables.EventHandler
import com.khater.rwaq.presentation.composables.RwaqBackButton
import com.khater.rwaq.presentation.composables.RwaqTopBar
import com.khater.rwaq.presentation.screens.productScreen.components.BrandDetailsSection
import com.khater.rwaq.presentation.screens.productScreen.components.ProductDetailsSheet
import com.khater.rwaq.presentation.screens.productScreen.components.ProductGrid
import com.khater.rwaq.presentation.screens.productScreen.components.SearchBottomSheet
import com.khater.rwaq.presentation.screens.productScreen.uiState.ProductScreenInteractionListener
import com.khater.rwaq.presentation.screens.productScreen.uiState.ProductScreenUiEffect
import com.khater.rwaq.presentation.screens.productScreen.uiState.ProductScreenUiState
import com.khater.rwaq.presentation.util.PaginationTrigger
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.empty_car
import rwaq.composeapp.generated.resources.no_products_available
import rwaq.composeapp.generated.resources.search
import rwaq.composeapp.generated.resources.something_went_wrong

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProductScreen(productViewModel: ProductViewModel = koinViewModel()) {
    val state = productViewModel.state.collectAsStateWithLifecycle().value
    val chatLazyListState = rememberLazyGridState()

    EventHandler(productViewModel.effect) { effect, navController ->
        when (effect) {
            ProductScreenUiEffect.NavigateBack -> navController.navigateUp()
        }
    }

    BackHandler(enabled = state.isDetailsVisible || state.isSearchVisible) {
        when {
            state.isDetailsVisible -> productViewModel.onDismissDetails()
            state.isSearchVisible -> productViewModel.onDismissSearch()
        }
    }

    ProductScreenContent(
        state = state,
        interactionListener = productViewModel as ProductScreenInteractionListener,
        chatLazyListState = chatLazyListState
    )

}

@Composable
fun ProductScreenContent(
    state: ProductScreenUiState,
    chatLazyListState: LazyGridState,
    interactionListener: ProductScreenInteractionListener,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        HomeScaffold(
            hasStatusBarColor = true,
            snackBarState = state.snackBar,
            topBar = {
                RwaqTopBar(
                    containerColor = Theme.colorScheme.brand.brand,
                    leadingContent = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Theme.spacing._8)
                        ) {
                            RwaqBackButton(
                                onClick = interactionListener::onBack, hasDropShadow = false,
                                tint = Theme.colorScheme.brand.onBrand,
                                modifier = Modifier.fillMaxHeight(),
                            )
                            RwaqBackButton(
                                painter = painterResource(Res.drawable.search),
                                onClick = interactionListener::onOpenSearch,
                                hasDropShadow = false,
                                tint = Theme.colorScheme.brand.onBrand,
                                modifier = Modifier.fillMaxHeight(),

                                )
                            BrandDetailsSection(
                                modifier = Modifier.weight(1f),
                                isPickupFromBranch = state.isPickupFormBranch,
                                carName = state.selectedCar.name,
                                branchName = state.branchName,
                                carNumber = state.selectedCar.carNumber
                            )
                        }
                    },

                    )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Theme.colorScheme.background.surface)
            ) {
                ProductContent(
                    state = state,
                    onProductClick = interactionListener::onProductClicked,
                    onRetry = interactionListener::onRetry,
                    listState = chatLazyListState
                )
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

//        ProductDetailsSheet(
//            isVisible = state.isDetailsVisible,
//            details = state.selectedProductDetails,
//            onDismiss = interactionListener::onDismissDetails,
//            listener = interactionListener
//        )

    }

    PaginationTrigger(
        list = state.groupedProducts.values.flatten(),
        listState = chatLazyListState,
        remainingItemsToLoadNextPage = 15,
        loadNextItems = interactionListener::onProductsScrolled
    )
}




@Composable
private fun ProductContent(
    state: ProductScreenUiState,
    onProductClick: (String) -> Unit,
    listState: LazyGridState,
    onRetry: () -> Unit,
) {
    when {
        state.isLoading && state.groupedProducts.isEmpty()  -> LoadingState()
        state.errorMessage != null && state.groupedProducts.isEmpty() -> ErrorState(state.errorMessage, onRetry)
        state.groupedProducts.values.flatten().isEmpty() -> EmptyState()
        else -> ProductGrid(state.groupedProducts, listState, onProductClick)
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        DotsProgressIndicator(dotSize = Theme.spacing._8)
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    EmptyOrErrorContent(
        painter = painterResource(Res.drawable.something_went_wrong),
        message = message,
        isError = true,
        onRetry = onRetry,
        modifier = Modifier.padding(Theme.spacing._49)
    )
}

@Composable
private fun EmptyState() {
    EmptyOrErrorContent(
        painter = painterResource(Res.drawable.empty_car),
        message = stringResource(Res.string.no_products_available),
        modifier = Modifier.padding(Theme.spacing._49)
    )
}
