package com.khater.rwaq.presentation.screens.productScreen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.component.text.Text
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.composables.RwaqBackButton
import com.khater.rwaq.presentation.composables.RwaqTopBar
import com.khater.rwaq.presentation.screens.productScreen.uiState.ProductUiModel
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.search_here

@Composable
fun SearchBottomSheet(
    isVisible: Boolean,
    searchQuery: String,
    filteredProducts: List<ProductUiModel>,
    onDismiss: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onProductClick: (String) -> Unit,
) {

    val groupedProducts = remember(filteredProducts) {
        filteredProducts.groupBy { it.category }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = Theme.colorScheme.brand.onBrand
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topEnd = 12.dp, topStart = 12.dp))
                    .background(Theme.colorScheme.brand.onBrand)
                    .padding(Theme.spacing._16)
                    .statusBarsPadding()
                    .systemBarsPadding()
                    .imePadding()
            )
            {
                RwaqTopBar(
                    leadingContent = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RwaqBackButton(onClick = onDismiss, modifier = Modifier.fillMaxHeight())
                            com.khater.rwaq.designSystem.component.textField.TextField(
                                value = searchQuery,
                                hint = stringResource(Res.string.search_here),
                                onValueChanged = onSearchQueryChange,
                                modifier = Modifier.weight(1f).background(Color.White),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    },

                    modifier = Modifier.padding(bottom = Theme.spacing._24)
                )

                Spacer(modifier = Modifier.height(8.dp))
// --- Content Area ---
                if (searchQuery.isNotEmpty() && filteredProducts.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "لا توجد نتائج",
                            style = Theme.typography.body.large,
                            color = Theme.colorScheme.shadePrimary
                        )
                    }
                } else {
                    // 2. Use LazyColumn for the Vertical List
                    LazyColumn(
                        contentPadding = PaddingValues(bottom = 24.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Iterate over the grouped map
                        groupedProducts.forEach { (categoryName, productsInCategory) ->

                            // A. The Category Header
                            item(key = "header_$categoryName") {
                                Text(
                                    text = categoryName,
                                    style = Theme.typography.title.medium, // Make this bold/larger
                                    color = Color.Gray,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    textAlign = TextAlign.Start // Or End if Arabic strictly
                                )
                            }

                            // B. The Product Items
                            items(productsInCategory, key = { it.id }) { product ->
                                ProductListRow(
                                    product = product,
                                    onClick = { onProductClick(product.id) }
                                )
                                // Optional: Add a separator line like the screenshot (if needed)
                                // HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                            }
                        }
                    }
                }

            }
        }
    }
}