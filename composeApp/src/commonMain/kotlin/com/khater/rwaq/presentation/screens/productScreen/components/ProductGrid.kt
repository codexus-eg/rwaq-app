package com.khater.rwaq.presentation.screens.productScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.component.text.Text
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.screens.productScreen.uiState.ProductUiModel

@Composable
fun ProductGrid(
    groupedProducts: Map<String, List<ProductUiModel>>,
    listState: LazyGridState,
    onProductClick: (String) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize(),
        state = listState
    ) {
        groupedProducts.forEach { (categoryName, products) ->
            item(
                key = "header_$categoryName",
                span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                CategoryHeader(title = categoryName)
            }

            items(products, key = { it.id }) { product ->
                ProductItem(
                    product = product,
                    onClick = { onProductClick(product.id) }
                )
            }
        }

    }
}

@Composable
fun CategoryHeader(title: String) {
    Text(
        text = title,
        style = Theme.typography.headline.small,
        color = Theme.colorScheme.primary.primary,
        modifier = Modifier
            .fillMaxWidth()
            .background(Theme.colorScheme.background.surface)
            .padding(top = 8.dp, bottom = 4.dp)
    )
}