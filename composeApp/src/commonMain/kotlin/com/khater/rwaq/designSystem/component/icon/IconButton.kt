package com.khater.rwaq.designSystem.component.icon

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.util.rippleIndication

@Composable
fun IconButton(
    painter: Painter,
    contentDescription: String?,
    shape: Shape = RoundedCornerShape(0.dp),
    containerColor: Color = Color.Unspecified,
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier.clip(shape)
            .background(containerColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rippleIndication()
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painter,
            tint = tint,
            contentDescription = contentDescription,
            modifier = iconModifier
        )
    }

}