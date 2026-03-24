package com.khater.rwaq.presentation.screens.rewardScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.khater.rwaq.designSystem.component.indicator.DotsProgressIndicator
import com.khater.rwaq.designSystem.component.scaffold.RwaqScaffold
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.domain.entities.product.Product
import com.khater.rwaq.presentation.composables.EmptyOrErrorContent
import com.khater.rwaq.presentation.composables.EventHandler
import com.khater.rwaq.presentation.composables.RwaqBackButton
import com.khater.rwaq.presentation.composables.RwaqTopBar
import com.khater.rwaq.presentation.composables.SnackBarContainer
import com.khater.rwaq.presentation.screens.homeScreen.components.CoffeeContent
import com.khater.rwaq.presentation.screens.productScreen.components.CategoryHeader
import com.khater.rwaq.presentation.screens.productScreen.components.ProductDetailsSheet
import com.khater.rwaq.presentation.screens.productScreen.components.ProductItem
import com.khater.rwaq.presentation.screens.productScreen.uiState.ProductUiModel
import com.khater.rwaq.presentation.screens.profileScreen.composables.SettingItemCard
import com.khater.rwaq.presentation.screens.profileScreen.composables.logoutDialog
import com.khater.rwaq.presentation.screens.rewardScreen.components.RewardProductDetailsSheet
import com.khater.rwaq.presentation.screens.rewardScreen.components.RewardProductItem
import com.khater.rwaq.presentation.screens.rewardScreen.uiState.RewardInteractionListener
import com.khater.rwaq.presentation.screens.rewardScreen.uiState.RewardsUiEffect
import com.khater.rwaq.presentation.screens.rewardScreen.uiState.RewardsUiState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.currency_sar
import rwaq.composeapp.generated.resources.empty_product
import rwaq.composeapp.generated.resources.empty_products
import rwaq.composeapp.generated.resources.profile_screen
import rwaq.composeapp.generated.resources.reward
import rwaq.composeapp.generated.resources.something_went_wrong
import rwaq.composeapp.generated.resources.wallet

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RewardScreen(rewardViewModel: RewardViewModel = koinViewModel()) {
    val lifecycleOwner = LocalLifecycleOwner.current

    // 1. Listen for when the user returns to this screen
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                // 2. Fetch the fresh points from the newly updated backend!
                rewardViewModel.onRefreshUserPoints()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    val state = rewardViewModel.state.collectAsStateWithLifecycle().value
    EventHandler(rewardViewModel.effect) { effect, controller ->
        when (effect) {
            is RewardsUiEffect.NavigateToProductDetails -> {}
            is RewardsUiEffect.NavigateBack -> {
                controller.navigateUp()
            }
        }
    }
    BackHandler(enabled = state.isDetailsVisible) {
        when {
            state.isDetailsVisible -> rewardViewModel.onDismissDetails()
        }
    }
    RewardContent(
        state = state,
        interactionListener = rewardViewModel as RewardInteractionListener
    )
}

@Composable
fun RewardContent(state: RewardsUiState, interactionListener: RewardInteractionListener) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        RwaqScaffold(
            hasStatusBarColor = true,
            snakeBar = { SnackBarContainer(state.snackBar) },
            topBar = {
                RwaqTopBar(
                    containerColor = Color(0xFFFBF7F0),
                    leadingContent = {
                        RwaqBackButton(
                            onClick = interactionListener::onBack,
                            hasDropShadow = true,
                            tint = Theme.colorScheme.shadePrimary
                        )
                    },
                    middleContent = {
                        com.khater.rwaq.designSystem.component.text.Text(
                            text = stringResource(Res.string.reward),
                            color = Theme.colorScheme.shadePrimary,
                            style = Theme.typography.headline.medium,
                            modifier = Modifier.offset(x = (-22).dp)
                        )
                    },
                    isCenterAligned = true,
                )
            }
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                SettingItemCard(
                    settingName = stringResource(Res.string.wallet),
                    settingSubName = "${state.points} ${stringResource(Res.string.currency_sar)}",
                    onClick = {},
                    hasNavigationIcon = false
                )
                when {
                    state.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            DotsProgressIndicator(dotSize = 8.dp)
                        }
                    }

                    state.errorMessage != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {

                            EmptyOrErrorContent(
                                message = state.errorMessage,
                                painter = painterResource(Res.drawable.something_went_wrong),
                                isError = true,
                                onRetry = interactionListener::onRetry
                            )
                        }
                    }

                    state.products.isEmpty() && !state.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {

                            EmptyOrErrorContent(
                                message = stringResource(Res.string.empty_products),
                                painter = painterResource(Res.drawable.empty_product),
                            )
                        }
                    }

                    else -> {
                        RewardProductGrid(
                            products = state.products,
                            listener = interactionListener
                        )
                    }
                }
            }
        }

        RewardProductDetailsSheet(
            isVisible = state.isDetailsVisible,
            details = state.selectedProductDetails,
            onDismiss = interactionListener::onDismissDetails,
            points = state.points,
            listener = interactionListener
        )
    }
}

@Composable
fun RewardProductGrid(
    products: List<Product>,
    listener: RewardInteractionListener,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(products, key = { it.id }) { product ->
            RewardProductItem(
                product = product,
                listener = listener
            )
        }
    }
}