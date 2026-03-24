package com.khater.rwaq.presentation.screens.orderScreen

import androidx.lifecycle.viewModelScope
import com.khater.rwaq.domain.entities.order.NewOrder
import com.khater.rwaq.domain.model.PagedData
import com.khater.rwaq.domain.useCases.CreateOrderUseCase
import com.khater.rwaq.presentation.base.BaseViewModel
import com.khater.rwaq.presentation.screens.orderScreen.uiState.OrderInteractionListener
import com.khater.rwaq.presentation.screens.orderScreen.uiState.OrderUiEffect
import com.khater.rwaq.presentation.screens.orderScreen.uiState.OrderUiState
import com.khater.rwaq.presentation.screens.orderScreen.uiState.toUiModel
import com.khater.rwaq.presentation.util.Paginator
import kotlinx.coroutines.launch

class OrderViewModel(
    private val getAllOrdersUseCase: CreateOrderUseCase
) : BaseViewModel<OrderUiState, OrderUiEffect>(OrderUiState()), OrderInteractionListener {

    private val orderPaginator by lazy {
        Paginator(
            initialKey = INITIAL_PAGE,
            onLoadUpdated = { isLoading ->
                // Only show full screen loading for the first page
                if (state.value.orders.isEmpty()) {
                    updateState { it.copy(isLoading = isLoading) }
                }
            },
            onRequest = ::getAllOrders,
            getNextKey = { currentPage, _ -> currentPage + 1 },
            onSuccess = { result, _ ->
                handleOrderSuccess(result)
            },
            endReached = { _, result -> result.isLastPage },
            onError = { error ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = error?.message
                    )
                }
            }
        )
    }

    init {
        loadNextPage()
    }

    private fun loadNextPage() {
        viewModelScope.launch {
            orderPaginator.loadNextItems()
        }
    }

    private suspend fun getAllOrders(page: Int): PagedData<NewOrder> {
        return getAllOrdersUseCase.getAllOrders(
            page = page,
            pageSize = PAGE_SIZE
        )
    }

    private fun handleOrderSuccess(result: PagedData<NewOrder>) {
        // Map Domain Entities to UI Models using the mapper we created earlier
        val newUiOrders = result.data.map { it.toUiModel() }

        updateState { currentState ->
            // Combine existing orders with new ones and remove duplicates
            val combinedOrders = (currentState.orders + newUiOrders)
                .distinctBy { it.id }

            currentState.copy(
                isLoading = false,
                errorMessage = null,
                orders = combinedOrders
            )
        }
    }

    override fun onRetry() {
        updateState { it.copy(isLoading = true, errorMessage = null) }
        orderPaginator.reset()
        loadNextPage()
    }


    override fun onBack() {
        sendNewEffect(OrderUiEffect.NavigateBack)
    }

    override fun onOrdersScrolled() {
        loadNextPage()    }

    companion object {
        const val PAGE_SIZE = 500 // Adjusted for orders, usually fewer per page than products
        const val INITIAL_PAGE = 1
    }
}