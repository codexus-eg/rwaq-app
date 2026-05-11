package com.khater.rwaq.presentation.screens.homeScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.adamglin.composeshadow.dropShadow
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.domain.entities.product.Product
import com.khater.rwaq.presentation.screens.homeScreen.uiStates.HomeScreenInteractionListener
import com.khater.rwaq.presentation.util.rippleClickable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.add_to_cart
import rwaq.composeapp.generated.resources.currency_sar
import rwaq.composeapp.generated.resources.plus

@Composable
fun ProductItem(
    product: Product,
    listener: HomeScreenInteractionListener,
    isAdding: Boolean = false
) {
    // 1. Calculate Prices
    val hasDiscount = product.discount > 0
    val finalPrice = if (hasDiscount) (product.basePrice - product.discount) else product.basePrice

    // Wrapper Box to handle Shadow independently
    Box(
        modifier = Modifier
            .fillMaxWidth()
            // Apply Shadow here (It draws outside the bounds)
            .dropShadow(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF001E14).copy(0.04f),
                blur = 20.dp,
                offsetY = 2.dp,
                offsetX = 0.dp
            )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                // Clip strictly for the Ripple effect inside
                .clip(RoundedCornerShape(16.dp))
                .rippleClickable(enabled = product.isInStock) { listener.onProductClicked(product.id) },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {

                // 2. Image (Network)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFAFAFA)),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                          //  .padding(8.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    // Optional: Out of Stock Badge
                    if (!product.isInStock) {
                        Surface(
                            color = Color.Black.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            Text(
                                text = "Out of Stock",
                                color = Color.White,
                                style = Theme.typography.body.small,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 3. Title & Description
                Text(
                    text = product.name,
                    style = Theme.typography.title.medium.copy(color = Color(0xff000D09)),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = product.description,
                    style = Theme.typography.body.small,
                    color = Color(0xffC4C4C4),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 4. Price Section
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Current Price (Red if discounted, Brown if normal)
                    Text(
                        text = "$finalPrice ${stringResource(Res.string.currency_sar)}",
                        style = Theme.typography.title.medium,
                        color = if (hasDiscount) Color(0xffFF0D0D) else Color(0xFF5C482E)
                    )

                    // Old Price (only if discounted)
                    if (hasDiscount) {
                        Spacer(modifier = Modifier.width(2.dp))

                        Text(
                            text = "${(product.basePrice)} ${stringResource(Res.string.currency_sar)}",
                            style = Theme.typography.body.small.copy(
                                textDecoration = TextDecoration.LineThrough
                            ),
                            color = Color(0xffC4C4C4)
                        )
                    }
//                    if (product.isInStock) {
                        Spacer(modifier = Modifier.weight(1f))

                        if (isAdding) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        color = Theme.colorScheme.brand.brand.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            }
                        } else {
                            IconButton(
                                onClick = {
                                    listener.onQuickAddToCart(product.id)
                                },
                                modifier = Modifier.size(32.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.add_to_cart),
                                    contentDescription = "Add to Cart",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(
                                            color = Theme.colorScheme.brand.brand,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(5.dp)
                                )
                            }
                        }
//                    }
                }
            }

        }
    }
}