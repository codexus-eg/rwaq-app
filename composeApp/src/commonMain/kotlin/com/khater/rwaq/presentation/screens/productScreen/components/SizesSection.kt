package com.khater.rwaq.presentation.screens.productScreen.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.component.text.Text
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.screens.productScreen.uiState.ProductSizeUiModel
import com.khater.rwaq.presentation.util.rippleClickable

@Composable
fun SizesSection(
    sizes: List<ProductSizeUiModel>,
    onSelectSize: (String) -> Unit,
) {
    Text(
        text = "الخيارات ( اختر 1 )",
        style = Theme.typography.body.large,
        color = Theme.colorScheme.primary.primary,
        fontWeight = FontWeight.SemiBold
    )
    Spacer(modifier = Modifier.height(8.dp))

    sizes.forEach { size ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .rippleClickable { onSelectSize(size.id) }
                .padding(all = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = size.isSelected,
                onClick = { onSelectSize(size.id) },
                colors = RadioButtonDefaults.colors(
                    selectedColor = Theme.colorScheme.brand.brand,
                    unselectedColor = Theme.colorScheme.shadePrimary
                )
            )
            Text(
                text = size.name,
                modifier = Modifier.weight(1f).padding(start = 8.dp),
                style = Theme.typography.body.medium,
                color = Theme.colorScheme.primary.primary
            )
            Text(
                text = "${size.price} SAR",
                style = Theme.typography.body.medium,
                color = Theme.colorScheme.shadePrimary
            )
        }
    }
}
