package com.khater.rwaq.presentation.screens.homeScreen.components

import androidx.compose.animation.animateBounds
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.first
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khater.rwaq.designSystem.component.indicator.DotsProgressIndicator
import com.khater.rwaq.designSystem.component.text.Text
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.screens.homeScreen.HomeViewModel
import com.khater.rwaq.presentation.screens.homeScreen.uiStates.HomeScreenInteractionListener
import com.khater.rwaq.presentation.screens.homeScreen.uiStates.HomeUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.coffee_tools
import rwaq.composeapp.generated.resources.special_offers

@Composable
fun CoffeeContent(
    state: HomeUiState,
    listener: HomeScreenInteractionListener,
    gridState: LazyGridState
) {

    val chipsListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Flag to prevent "fighting" during click animations
    var isProgrammaticScroll by remember { mutableStateOf(false) }
//    val currentVisibleCategory by remember {
//        derivedStateOf {
//            val firstVisibleIndex = gridState.firstVisibleItemIndex
//            val firstVisibleOffset = gridState.firstVisibleItemScrollOffset
//
//            val firstVisibleItemInfo = gridState.layoutInfo.visibleItemsInfo.firstOrNull()
//            val itemHeight = firstVisibleItemInfo?.size?.height ?: 1
//            val offsetThreshold = itemHeight * 0.10f
//
//            val effectiveIndex = if (firstVisibleOffset >= offsetThreshold) {
//                firstVisibleIndex
//            } else {
//                (firstVisibleIndex - 1).coerceAtLeast(0)
//            }
//
//            if (effectiveIndex <= 1) {
//                state.categoryDisplayOrder.firstOrNull()
//            } else {
//                state.categoryScrollIndices.entries
//                    .filter { it.value <= effectiveIndex }
//                    .maxByOrNull { it.value }
//                    ?.key
//            }
//        }
//    }
    // Track the current visible category
    val currentVisibleCategory by remember {
        derivedStateOf {
            val firstVisibleIndex = gridState.firstVisibleItemIndex
            val firstVisibleOffset = gridState.firstVisibleItemScrollOffset

            // If we're at the very top (index 0 or 1 = profile/search section)
            // Select the first category
            if (firstVisibleIndex <= 1) {
                state.categoryDisplayOrder.firstOrNull()
            } else {
                // Find category with tolerance for partially visible items
                state.categoryScrollIndices.entries
                    .filter { it.value <= firstVisibleIndex }
                    .maxByOrNull { it.value }
                    ?.key
            }
        }
    }

    // Sync category selection with scroll position (user scrolling)
    // Update immediately during scroll for instant chip feedback
    LaunchedEffect(currentVisibleCategory) {
        if (!isProgrammaticScroll &&
            currentVisibleCategory != null &&
            currentVisibleCategory != state.selectedCategoryId
        ) {
            (listener as? HomeViewModel)?.onManualScroll(currentVisibleCategory!!)

            // Also scroll chips to the beginning when at top
            if (gridState.firstVisibleItemIndex <= 1) {
                scope.launch {
                    chipsListState.animateScrollToItem(0, scrollOffset = 0)
                }
            }
        }
    }
    LaunchedEffect(state.selectedCategoryId) {
        val index = state.categories.indexOfFirst { it.id == state.selectedCategoryId }
        if (index >= 0) {
            // Just sync the chips row seamlessly
            if (index == 0) {
                chipsListState.animateScrollToItem(0, scrollOffset = 0)
            } else {
                val offset = -100
                chipsListState.animateScrollToItem(index = index, scrollOffset = offset)
            }
        }
    }
    // Sync chips row when category is clicked
//    LaunchedEffect(state.selectedCategoryId) {
//        val index = state.categories.indexOfFirst { it.id == state.selectedCategoryId }
//        if (index >= 0) {
//            isProgrammaticScroll = true
//
//            scope.launch {
//                try {
//                    // Scroll chip to visible position
//                    if (index == 0) {
//                        // First chip - scroll to beginning
//                        chipsListState.animateScrollToItem(0, scrollOffset = 0)
//                    } else {
//                        // Center the selected chip
//                        val offset = -100
//                        chipsListState.animateScrollToItem(
//                            index = index,
//                            scrollOffset = offset
//                        )
//                    }
//                } finally {
//                    // Small delay before releasing the lock
//                    delay(300)
//                    isProgrammaticScroll = false
//                }
//            }
//        }
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            //  .background(Color(0xFFFBF7F0))
            //   .verticalScroll(rememberScrollState())
            .padding(bottom = 4.dp)
    ) {

        Spacer(modifier = Modifier.height(16.dp))


        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item(
                span = { GridItemSpan(maxLineSpan) }
            ) {
                Column {
                    ProfileSection(
                        username = state.userName,
                    )
                    SearchBarSection(
                        onClick = listener::onOpenSearch,
                        shouldClearFocus = state.isSearchVisible
                    )
                }
            }

            stickyHeader(key = "chips_row") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                       .background(Theme.colorScheme.brand.onBrand) // Match background to hide content behind
                       .padding(bottom = 6.dp)
                ) {
                    LazyRow(
                        state = chipsListState, // <--- Attach the State here
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        // Remove padding here since the Box handles the background area
                    ) {
                        items(state.categories, key = {it.id}) { category ->
                            val isSelected = state.selectedCategoryId == category.id
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    if (!isSelected) { // Prevent re-clicking the same chip
                                        // 1. Lock the manual scroll observer
                                        isProgrammaticScroll = true

                                        // 2. Trigger the scroll effect in the ViewModel
                                        listener.onSelectCategory(category.id)

                                        // 3. Unlock ONLY when the grid finishes scrolling
                                        scope.launch {
                                            // Wait a bit to let the ViewModel's delay(50) + effect reach the screen
                                            delay(200)

                                            // Wait dynamically until the scroll animation is fully completed
                                            snapshotFlow { gridState.isScrollInProgress }
                                                .first { isScrolling -> !isScrolling }

                                            // Now it's safe to unlock
                                            isProgrammaticScroll = false
                                        }
                                    }
                                },
                                label = { Text(category.name, style = Theme.typography.body.medium) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFFC69C6D),
                                    selectedLabelColor = Color.White,
                                    containerColor = Color.Transparent,
                                    labelColor = Color.Gray
                                ),
                                border = if (isSelected) null else FilterChipDefaults.filterChipBorder(
                                    borderColor = Color(0xFFE0E0E0), borderWidth = 1.dp,
                                    enabled = true, selected = false
                                ),
                                shape = RoundedCornerShape(50),
                                modifier = Modifier.height(40.dp)
                            )
                        }
                    }
                }
            }

            state.categoryDisplayOrder.forEach { categoryId ->
                val products = state.groupedProducts[categoryId] ?: emptyList()

                if (products.isNotEmpty()) {
                    item(
                        span = { GridItemSpan(maxLineSpan) },
                        key = "header_$categoryId"
                    ) {
                        // Check if this is our virtual ID
                        val title = if (categoryId == "Special Offers") {
                            stringResource(Res.string.special_offers) // Localized String
                        } else {
                            categoryId // Normal Category Name from Backend
                        }

                        SectionHeader(title = title)
                    }

                    items(items = products, key = { it.id + categoryId }) { product ->
                        // Note: I added "+ categoryId" to the key above.
                        // Since a product is now duplicated (once in Offers, once in Coffee),
                        // using just 'product.id' as a key might cause a crash or UI glitch.
                        // Making the key unique per section fixes this.

                        Box(modifier = Modifier.animateItem()) {
                            ProductItem(product = product, listener = listener)
                        }
                    }
                }
            }

//            item(
//                span = { GridItemSpan(maxLineSpan) }
//            ) {
//                LazyRow(
//                    horizontalArrangement = Arrangement.spacedBy(12.dp),
//                    contentPadding = PaddingValues(horizontal = 16.dp)
//                ) {
//                    items(state.categories) { category ->
//                        val isSelected = state.selectedCategoryId == category.id
//                        FilterChip(
//                            selected = isSelected,
//                            onClick = { listener.onSelectCategory(category.id) },
//                            label = { Text(category.name, style = Theme.typography.body.medium) },
//                            colors = FilterChipDefaults.filterChipColors(
//                                selectedContainerColor = Color(0xFFC69C6D),
//                                selectedLabelColor = Color.White,
//                                containerColor = Color.Transparent,
//                                labelColor = Color.Gray
//                            ),
//                            border = if (isSelected) null else FilterChipDefaults.filterChipBorder(
//                                borderColor = Color(0xFFE0E0E0), borderWidth = 1.dp,
//                                enabled = true, selected = false
//                            ),
//                            shape = RoundedCornerShape(50)
//                        )
//                    }
//                }
//            }
//            items(
//                items = state.displayedCoffeeProducts,
//                key = { product -> product.id }
//            ) { product ->
//                Box(
//                    modifier = Modifier
//                        .animateItem()
//                ) {
//                    ProductItem(product = product, listener = listener)
//                }
//            }
//
//            // special offers
//            if (state.specialOffers.isNotEmpty()) {
//                stickyHeader {
//                    SectionHeader(title = stringResource(Res.string.special_offers))
//                }
//                items(state.specialOffers) { product ->
//                    ProductItem(product = product, listener = listener)
//                }
//            }
//
//
//            // other sections
//            if (state.otherSections.isNotEmpty()) {
//                state.otherSections.forEach { (categoryName, products) ->
//
//                    stickyHeader {
//                        SectionHeader(title = categoryName)
//                    }
//                    items(products) { product ->
//                        ProductItem(product = product, listener = listener)
//                    }
//                }
//
//            }
//
//            // machines
//            if (state.machineProducts.isNotEmpty()) {
//                stickyHeader {
//                    SectionHeader(title = stringResource(Res.string.coffee_tools))
//                }
//                items(state.machineProducts) { product ->
//                    ProductItem(product = product, listener = listener)
//                }
//            }

        }
        Spacer(modifier = Modifier.height(4.dp))

         //  }

//
//        // ==========================================
//        // 2. SPECIAL OFFERS (Vertical Stack)
//        // ==========================================
//        if (state.specialOffers.isNotEmpty()) {
//            SectionHeader(title = stringResource(Res.string.special_offers)) // Helper function below
//
//            Column(
//                verticalArrangement = Arrangement.spacedBy(16.dp),
//                modifier = Modifier.padding(horizontal = 16.dp)
//            ) {
//                state.specialOffers.forEach { product ->
//                    SpecialOfferItem(product = product, listener = listener)
//                }
//            }
//            Spacer(modifier = Modifier.height(24.dp))
//        }
//
//        // ==========================================
//        // 3. OTHER CATEGORIES (Dynamic Sections)
//        // ==========================================
//        // Iterate over the map: "Cake" -> List, "Dessert" -> List
//        state.otherSections.forEach { (categoryName, products) ->
//            if (products.isNotEmpty()) {
//
//                SectionHeader(title = categoryName)
//
//                LazyRow(
//                    contentPadding = PaddingValues(horizontal = 16.dp),
//                    horizontalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    items(
//                        items = products,
//                        key = { it.id }
//                    ) { product ->
//                        Box(modifier = Modifier.width(160.dp)) {
//                            ProductItem(product = product, listener = listener)
//                        }
//                    }
//                }
//                Spacer(modifier = Modifier.height(24.dp))
//            }
//        }
//
//        // ==========================================
//        // 4. MACHINES SECTION
//        // ==========================================
//        if (state.machineProducts.isNotEmpty()) {
//            SectionHeader(title = stringResource(Res.string.coffee_tools))
//
//            LazyRow(
//                contentPadding = PaddingValues(horizontal = 16.dp),
//                horizontalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                items(
//                    items = state.machineProducts,
//                    key = { it.id }
//                ) { product ->
//                    Box(modifier = Modifier.width(160.dp)) {
//                        ProductItem(product = product, listener = listener)
//                    }
//                }
//            }
//            Spacer(modifier = Modifier.height(30.dp))
//        }
    }
}

// Helper Composable for Section Titles
@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = Theme.typography.title.large.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF1A120A)
        ),
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
    )
}