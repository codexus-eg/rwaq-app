package com.khater.rwaq.presentation.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.khater.rwaq.designSystem.util.rippleIndication

fun Modifier.rippleClickable(
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }

    this.clickable(
        interactionSource = interactionSource,
        indication = rippleIndication(),
        onClick = onClick,
        enabled = enabled
    )
}