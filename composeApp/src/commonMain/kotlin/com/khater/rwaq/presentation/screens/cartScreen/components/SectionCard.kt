package com.khater.rwaq.presentation.screens.cartScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.adamglin.composeshadow.dropShadow
import com.khater.rwaq.designSystem.component.text.Text
import com.khater.rwaq.designSystem.theme.theme.Theme

@Composable
fun SectionCard(
    title: String?,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
         modifier = modifier.fillMaxWidth()
             .dropShadow(
                 shape = RoundedCornerShape(16.dp),
                 color = Color(0xFF001E14).copy(0.04f),
                 blur = 20.dp,
                 offsetY = 2.dp,
                 offsetX = 0.dp
             )
             .clip(RoundedCornerShape(12.dp))
             .background(Theme.colorScheme.brand.onBrand)
             .padding(16.dp)
    ) {
            if (title != null) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(title, style = Theme.typography.body.medium, color = Theme.colorScheme.primary.primary)
                    if (actionText != null && onActionClick != null) {
                        Row(modifier = Modifier.clickable { onActionClick() }, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.AddCircleOutline, contentDescription = null, tint = Theme.colorScheme.brand.brand, modifier = Modifier.size(16.dp))
                             Text(actionText, color = Theme.colorScheme.brand.brand, style = Theme.typography.body.small)
                        }
                    }
                }
            }
            content()
    }
}
