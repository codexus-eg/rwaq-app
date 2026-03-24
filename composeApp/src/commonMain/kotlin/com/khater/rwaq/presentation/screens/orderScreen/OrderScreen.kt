package com.khater.rwaq.presentation.screens.orderScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.khater.rwaq.designSystem.component.indicator.DotsProgressIndicator
import com.khater.rwaq.designSystem.component.scaffold.HomeScaffold
import com.khater.rwaq.designSystem.component.text.Text
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.composables.EmptyOrErrorContent
import com.khater.rwaq.presentation.composables.EventHandler
import com.khater.rwaq.presentation.composables.RwaqBackButton
import com.khater.rwaq.presentation.composables.RwaqTopBar
import com.khater.rwaq.presentation.screens.orderScreen.components.OrderCard
import com.khater.rwaq.presentation.screens.orderScreen.uiState.OrderInteractionListener
import com.khater.rwaq.presentation.screens.orderScreen.uiState.OrderUiEffect
import com.khater.rwaq.presentation.screens.orderScreen.uiState.OrderUiState
import com.khater.rwaq.presentation.util.PaginationTrigger
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.empty_product
import rwaq.composeapp.generated.resources.my_orders
import rwaq.composeapp.generated.resources.no_orders_to_view
import rwaq.composeapp.generated.resources.something_went_wrong

@Composable
fun OrderScreen(
    orderViewModel: OrderViewModel = koinViewModel()
){
    val state = orderViewModel.state.collectAsStateWithLifecycle().value
    EventHandler(orderViewModel.effect) {effect,controller->
        when(effect){
            OrderUiEffect.NavigateBack -> controller.navigateUp()
        }
    }

    OrderContent(
        state = state,
        interaction = orderViewModel as OrderInteractionListener
    )

}

@Composable
fun OrderContent(
    state: OrderUiState,
    interaction: OrderInteractionListener
) {
    val listState = rememberLazyListState()

    Box(
        modifier = Modifier.fillMaxSize().imePadding(),
        contentAlignment = Alignment.Center
    ) {

        HomeScaffold(
            hasStatusBarColor = true,
            snackBarState = state.snackBar,
            topBar = {
                RwaqTopBar(
                    containerColor = Color(0xFFFBF7F0),
                    leadingContent = {
                        RwaqBackButton(
                            onClick = interaction::onBack,
                            hasDropShadow = true,
                            tint = Theme.colorScheme.shadePrimary
                        )
                    },
                    middleContent = {
                        Text(
                            text = stringResource(Res.string.my_orders),
                            color = Theme.colorScheme.shadePrimary,
                            style = Theme.typography.headline.medium,
                            modifier = Modifier.offset(x = (-22).dp)
                        )
                    },
                    isCenterAligned = true,
                )
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Handling States
                when {
                    // 1. Initial Loading
                    state.isLoading && state.orders.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            DotsProgressIndicator(dotSize = Theme.spacing._8)
                        }
                    }

                    // 2. Error State
                    state.errorMessage != null && state.orders.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            EmptyOrErrorContent(
                                painter = painterResource(Res.drawable.something_went_wrong),
                                message = state.errorMessage,
                                isError = true,
                                onRetry = interaction::onRetry
                            )
                        }
                    }

                    // 3. Empty State
                    state.orders.isEmpty() && !state.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            EmptyOrErrorContent(
                                painter = painterResource(Res.drawable.empty_product), // Use appropriate empty resource
                                message = stringResource(Res.string.no_orders_to_view), // Add string resource here
                                imageSize = 200.dp
                            )
                        }
                    }

                    // 4. Success State (List of Orders)
                    else -> {

                        // Pagination Trigger logic
                        val shouldLoadMore = remember {
                            derivedStateOf {
                                val totalItemsCount = listState.layoutInfo.totalItemsCount
                                val lastVisibleItemIndex =
                                    listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

                                // Trigger when within 2 items of the end
                                lastVisibleItemIndex >= (totalItemsCount - 2)
                            }
                        }

                        LaunchedEffect(shouldLoadMore.value) {
                            if (shouldLoadMore.value && !state.isLoading) {
                                interaction.onOrdersScrolled()
                            }
                        }

                        LazyColumn(
                            state = listState,
                            contentPadding = PaddingValues(vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(items = state.orders, key = { it.id }) { order ->
                                OrderCard(order = order)
                            }

                            // Bottom Loading Indicator for Pagination
                            if (state.isLoading && state.orders.isNotEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        DotsProgressIndicator(dotSize = 6.dp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }

        PaginationTrigger(
            list = state.orders,
            listState = listState,
            remainingItemsToLoadNextPage = 15,
            loadNextItems = interaction::onOrdersScrolled
        )
    }
 }