package com.khater.rwaq.presentation.screens.profileScreen.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.adamglin.composeshadow.dropShadow
import com.khater.rwaq.presentation.screens.profileScreen.uiState.SettingItem
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsGroup(
    items: List<SettingItem>,
    modifier: Modifier = Modifier,
    onItemClick: (SettingItem) -> Unit
) {
    // The container card gives the rounded look to the whole group
    Card(
        modifier = modifier.fillMaxWidth() .dropShadow(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF001E14).copy(0.04f),
            blur = 20.dp,
            offsetY = 2.dp,
            offsetX = 0.dp
        ),
        shape = RoundedCornerShape(24.dp), // Match your theme spacing
        colors = CardDefaults.cardColors(
            containerColor = Color.White // Or Theme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // Flat style like image
    ) {
        Column {
            items.forEachIndexed { index, item ->
                SettingItemCard(
                    settingName = stringResource(item.titleRes),
                    settingSubName = item.settingSubName,
                    // Important: Make item square so it fits flush in the group
                    shape = RectangleShape, 
                    // Optional: Transparent background so it uses the Card's white color
                    containerColor = Color.Transparent, 
                    onClick = { onItemClick(item) }
                )

                // Add a divider only if it's NOT the last item
                if (index < items.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp), // Indent divider
                        thickness = 0.5.dp,
                        color = Color.LightGray.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}