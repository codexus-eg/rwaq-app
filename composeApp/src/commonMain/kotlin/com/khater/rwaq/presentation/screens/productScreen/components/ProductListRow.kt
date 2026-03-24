package com.khater.rwaq.presentation.screens.productScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.khater.rwaq.designSystem.component.text.Text
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.screens.productScreen.uiState.ProductUiModel
import com.khater.rwaq.presentation.util.rippleClickable

// --- Helper Composable: The Row Design from Screenshot ---
@Composable
fun ProductListRow(
    product: ProductUiModel,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .rippleClickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AsyncImage(
            model = product.imageUrl,
            contentDescription = product.name,
            contentScale = ContentScale.Inside,
             modifier = Modifier
                .size(100.dp)
                 .dropShadow(
                     shape = RoundedCornerShape(Theme.spacing._12),
                     shadow = androidx.compose.ui.graphics.shadow.Shadow(
                         radius = 2.dp,
                         color = Color.Black.copy(0.08f),
                         offset = _root_ide_package_.androidx.compose.ui.unit.DpOffset(
                             x = 3.dp,
                             y = 3.dp
                         )
                     )
                 )
                 .clip(RoundedCornerShape(8.dp)) // Rounded corners
                .background(Theme.colorScheme.brand.onBrand) // Placeholder color
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = product.name, // e.g., "كرواسون"
                style = Theme.typography.body.large,
                color = Theme.colorScheme.primary.primary
            )
            
            Text(
                text = "${product.price} SAR", // e.g., "7.00 SAR"
                style = Theme.typography.body.medium,
                color = Theme.colorScheme.shadePrimary
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Right Side: Image
        // Use your image loader (AsyncImage from Coil, or similar)
        // Since I don't have your image loader, I'll use a placeholder Box or basic Image

    }
}