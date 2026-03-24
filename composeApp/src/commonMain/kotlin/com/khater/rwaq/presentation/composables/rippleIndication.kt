package com.khater.rwaq.presentation.composables

import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.material3.ripple
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

fun rippleIndication(
    bounded: Boolean = true,
    radius: Dp = Dp.Unspecified,
    color: Color = Color.Unspecified
): IndicationNodeFactory {
    return ripple(bounded, radius, color)
}