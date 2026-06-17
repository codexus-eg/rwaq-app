package com.khater.rwaq.presentation.screens.productScreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.component.text.Text
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.screens.productScreen.uiState.ProductExtensionUiModel
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.currency_sar
import rwaq.composeapp.generated.resources.extensions

@Composable
fun ExtensionsSection(
    extensions: List<ProductExtensionUiModel>,
    onQuantityChange: (String, Int) -> Unit,
) {
    Text(
        text = stringResource(Res.string.extensions),
        style = Theme.typography.body.large,
        color = Theme.colorScheme.primary.primary,
        fontWeight = FontWeight.SemiBold
    )
    Spacer(modifier = Modifier.height(16.dp))

    extensions.forEach { ext ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = ext.name,
                    style = Theme.typography.body.medium,
                    color = Theme.colorScheme.primary.primary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${ext.price} ${stringResource(Res.string.currency_sar)}",
                    style = Theme.typography.body.small,
                    color = Theme.colorScheme.shadePrimary
                )

            }

            QuantitySelector(
                count = ext.currentQty,
                maxCount = ext.maxCount,
                minCount = 0,
                onDecrease = { onQuantityChange(ext.id, -1) },
                onIncrease = { onQuantityChange(ext.id, 1) }
            )
        }
        HorizontalDivider(color = Color(0xFFF1F1F1))
    }
}