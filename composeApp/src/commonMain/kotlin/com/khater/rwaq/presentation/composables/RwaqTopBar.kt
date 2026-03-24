package com.khater.rwaq.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.theme.theme.Theme

@Composable
fun RwaqTopBar(
    leadingContent: @Composable () -> Unit = {},
    trailingContent: @Composable () -> Unit = {},
    middleContent: @Composable () -> Unit = {},
    isCenterAligned: Boolean = false,
    containerColor: Color = Color.Transparent,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(containerColor)
            .padding(start = Theme.spacing._16, end = Theme.spacing._16, bottom = Theme.spacing._8)
            .height(56.dp),
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