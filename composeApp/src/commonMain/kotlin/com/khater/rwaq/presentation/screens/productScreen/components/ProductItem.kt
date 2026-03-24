package com.khater.rwaq.presentation.screens.productScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.khater.rwaq.designSystem.component.text.Text
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.screens.productScreen.uiState.ProductUiModel
import com.khater.rwaq.presentation.util.rippleClickable

@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    product: ProductUiModel,
    onClick: () -> Unit,
) {

    Column(
        modifier = modifier

            .dropShadow(
                shape = RoundedCornerShape(Theme.spacing._12),
                shadow = androidx.compose.ui.graphics.shadow.Shadow(
                    radius = 2.dp,
                    color = Color.Black.copy(0.05f),
                    offset = _root_ide_package_.androidx.compose.ui.unit.DpOffset(
                        x = 0.dp,
                        y = 1.dp
                    )
                )
            )
            .clip(RoundedCornerShape(Theme.spacing._12))
            .background(Theme.colorScheme.brand.onBrand)
            .rippleClickable { onClick() }
            .padding(Theme.spacing._16),
    ) {
        AsyncImage(
            model = product.imageUrl,
            contentDescription = product.name,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .clip(RoundedCornerShape(12.dp))
                .height(140.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Inside
        )
        Text(
            text = product.name,
            style = Theme.typography.body.medium,
            color = Theme.colorScheme.primary.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row(
            modifier = Modifier.padding(top = Theme.spacing._8),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Theme.spacing._8)
        ) {
            Text(
                text = product.formattedPrice,
                style = Theme.typography.body.medium,
                color = Theme.colorScheme.primary.primary,
            )
            if (product.discount != "0.0") {
                Text(
                    text = "${product.discount} SAR",
                    style = Theme.typography.body.small.copy(
                        textDecoration = TextDecoration.LineThrough
                    ),
                    color = Theme.colorScheme.shadePrimary
                )
            }

        }

    }
}