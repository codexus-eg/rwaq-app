package com.khater.rwaq.presentation.screens.profileScreen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.component.icon.Icon
import com.khater.rwaq.designSystem.component.text.Text
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.designSystem.util.rippleIndication
import com.khater.rwaq.domain.util.AppLanguage
import com.khater.rwaq.presentation.util.mapLanguage

import org.jetbrains.compose.resources.stringResource

@Composable
fun LanguageOptionItem(
    isSelected: Boolean,
    selectedAppLanguage: AppLanguage,
    icon: Painter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing._4, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(Theme.spacing._24))
            .background(
                color = if (isSelected) Theme.colorScheme.brand.onBrandVariant else Color.Transparent,
                shape = RoundedCornerShape(Theme.spacing._24)
            )
            .border(1.dp,if (isSelected) Theme.colorScheme.brand.brandVariant else Color.Transparent, RoundedCornerShape(Theme.spacing._24))
            .clickable(
                enabled = !isSelected,
                onClick = {
                    onClick()
                },
                indication = rippleIndication(),
                interactionSource = remember { MutableInteractionSource() })
            .padding(horizontal = Theme.spacing._32, vertical = Theme.spacing._24)
    ) {
        Text(
            text = stringResource(mapLanguage(selectedAppLanguage.iso)),
            color = Theme.colorScheme.primary.primary,
            style = Theme.typography.body.medium,
        )
        Icon(
            painter = icon,
            contentDescription = null,
            tint = Theme.colorScheme.brand.brandVariant,
        )

    }
}


