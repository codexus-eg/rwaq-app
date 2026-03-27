package com.khater.rwaq.presentation.screens.profileScreen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.composables.rippleIndication
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.logo
import rwaq.composeapp.generated.resources.logout
import rwaq.composeapp.generated.resources.navigate_icon

@Composable
fun SettingItemCard(
    settingName: String,
    settingSubName: String?,
    modifier: Modifier = Modifier,
    isLogout:Boolean = false,
    hasNavigationIcon: Boolean  = true,
    textColor: Color = Theme.colorScheme.primary.primary,
    containerColor: Color = Theme.colorScheme.brand.onBrand,
    shape: Shape = RoundedCornerShape(Theme.spacing._12),
    icon: DrawableResource = Res.drawable.navigate_icon,
    onClick: () -> Unit,
) {
    val layoutDirection = LocalLayoutDirection.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rippleIndication()
            ) { onClick() }
            .background(containerColor)
            .padding(Theme.spacing._12),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .padding(start = Theme.spacing._8)
                .then(
                    if (settingSubName == null) Modifier.padding(vertical = 10.dp) else Modifier
                )
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = settingName,
                style = Theme.typography.body.small,
                color = textColor
            )
            settingSubName?.let {
                Text(
                    text = settingSubName,
                    style = Theme.typography.body.extraSmall,
                    color = textColor.copy(0.5f)
                )
            }
        }

        if (hasNavigationIcon){

            Icon(
                painter = painterResource(icon),
                contentDescription = settingName,
                modifier = Modifier.size(24.dp).graphicsLayer {
                    if (layoutDirection == LayoutDirection.Ltr) {
                        scaleX = -1f
                    }
                },
                tint = if (isLogout) Theme.colorScheme.error else Theme.colorScheme.secondary.secondary
            )
        }

    }
}