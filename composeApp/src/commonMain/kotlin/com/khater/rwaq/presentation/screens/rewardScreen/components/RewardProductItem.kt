package com.khater.rwaq.presentation.screens.rewardScreen.components

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.adamglin.composeshadow.dropShadow
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.domain.entities.product.Product
import com.khater.rwaq.presentation.screens.homeScreen.uiStates.HomeScreenInteractionListener
import com.khater.rwaq.presentation.screens.rewardScreen.uiState.RewardInteractionListener
import com.khater.rwaq.presentation.util.rippleClickable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.add_to_cart
import rwaq.composeapp.generated.resources.point

@Composable
fun RewardProductItem(
    product: Product,
    listener: RewardInteractionListener,
    points: Double = Double.POSITIVE_INFINITY,
    isAdding: Boolean = false,
) {

    // 1. Reward cost in points (compared against the user's points balance)
    val rewardPoints = product.points
    val canQuickAdd = product.isInStock && points >= rewardPoints

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
                .rippleClickable { listener.onProductClicked(product.id) },
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

                // 4. Points Section
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Reward cost in points
                    Text(
                        text = "${rewardPoints.toInt()} ${stringResource(Res.string.point)}",
                        style = Theme.typography.title.medium,
                        color = Color(0xFF5C482E)
                    )

                  //  if (product.isInStock) {
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
                                    if (canQuickAdd) listener.onQuickAddToCart(product.id)
                                },
                                enabled = canQuickAdd,
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
                                            color = if (canQuickAdd) Theme.colorScheme.brand.brand else Color.LightGray,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(5.dp)
                                )
                            }
                        }
                   // }
                }
            }
//            if (product.isInStock) {
//                IconButton(
//                    onClick = {
//                        listener.onQuickAddToCart(product.id)
//                    },
//                    modifier = Modifier.size(32.dp)
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Add,
//                        contentDescription = "Add to Cart",
//                        tint = Color.White,
//                        modifier = Modifier
//                            .size(32.dp)
//                            .background(
//                                color = Color(0xFFC69C6D),
//                                shape = RoundedCornerShape(8.dp)
//                            )
//                            .padding(6.dp)
//                    )
//                }
//            }
        }
    }
}
