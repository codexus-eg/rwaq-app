package com.khater.rwaq.presentation.screens.homeScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.util.rippleClickable

@Composable
fun ButtonWithIcon(
    leadingContent: @Composable () -> Unit = {},
    trailingContent: @Composable () -> Unit = {},
    middleContent: @Composable () -> Unit = {},
    isCenterAligned: Boolean = false,
    containerColor: Color = Theme.colorScheme.brand.brand,
    modifier: Modifier = Modifier,
    onClick:()-> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Theme.spacing._12))
            .background(containerColor)
            .rippleClickable {
                onClick()
            }
            .padding(horizontal = Theme.spacing._16)
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingContent()
        Spacer(Modifier.weight(1f))
        middleContent()
        if (isCenterAligned)
            Spacer(Modifier.weight(1f))
        trailingContent()
    }
}