package com.khater.rwaq.presentation.screens.homeScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.adamglin.composeshadow.dropShadow
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.domain.entities.product.Product
import com.khater.rwaq.presentation.screens.homeScreen.uiStates.HomeScreenInteractionListener
import com.khater.rwaq.presentation.util.rippleClickable
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.currency_sar

@Composable
fun SpecialOfferItem(
    product: Product,
    listener: HomeScreenInteractionListener
) {
    // 1. Calculate Discount Price
    val hasDiscount = product.discount > 0
    val finalPrice = if (hasDiscount) (product.basePrice - product.discount) else product.basePrice

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .dropShadow(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF001E14).copy(0.04f),
                blur = 20.dp,
                offsetY = 2.dp,
                offsetX = 0.dp
            )
            .clip(RoundedCornerShape(16.dp))
            .rippleClickable { listener.onProductClicked(product.id) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        // ROW LAYOUT: Image Left, Details Right
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            
            // --- 2. Image Section (Left) ---
            Box(
                modifier = Modifier
                    .size(100.dp) // Fixed square size for the image
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFAFAFA)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )

                if (!product.isInStock) {
                    Surface(
                        color = Color.Black.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "Out of Stock",
                            color = Color.White,
                            style = Theme.typography.body.small,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // --- 3. Details Section (Right) ---
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Title
                Text(
                    text = product.name,
                    style = Theme.typography.title.medium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xff000D09)
                    ),
                    maxLines = 1
                )
                
                // Description (Roast level etc)
                Text(
                    text = product.description,
                    style = Theme.typography.body.small,
                    color = Color(0xffC4C4C4),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Price Row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Current Price (Red if discounted, Brown if normal)
                    Text(
                        text = "$finalPrice ${stringResource(Res.string.currency_sar)}",
                        style = Theme.typography.title.medium,
                        color = if (hasDiscount) Color(0xffFF0D0D) else Color(0xFF5C482E)
                    )

                    // Old Price (only if discounted)
                    if (hasDiscount) {
                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "${(product.basePrice)} ${stringResource(Res.string.currency_sar)}",
                            style = Theme.typography.body.small.copy(
                                textDecoration = TextDecoration.LineThrough
                            ),
                            color = Color(0xffC4C4C4)
                        )
                    }
                }
            }
        }
    }
}