package com.khater.rwaq.presentation.screens.branchScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.component.icon.Icon
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.designSystem.util.rippleIndication

@Composable
fun BranchIcon(
    modifier: Modifier = Modifier,
    onClick:() -> Unit,
    scale: Float = 1f,
    shape: Shape = CircleShape,
    painter: Painter
){
    Box(
    modifier = modifier
        .size(30.dp)
        .clip(shape)
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rippleIndication(),
            onClick = onClick
        )  ,
    contentAlignment = Alignment.Center
) {
    Icon(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .size(24.dp).scale(scale),
        tint = Theme.colorScheme.shadePrimary
    )
}

}