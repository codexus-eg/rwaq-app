package com.khater.rwaq.designSystem.theme.spacing

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Spacing(
    val _2: Dp = 2.dp,
    val _4: Dp = 4.dp,
    val _6: Dp = 6.dp,
    val _8: Dp = 8.dp,
    val _12: Dp = 12.dp,
    val _16: Dp = 16.dp,
    val _24: Dp = 24.dp,
    val _32: Dp = 32.dp,
    val _49: Dp = 49.dp
)

internal val AquaSpacing = Spacing()
internal val LocalSpacing = staticCompositionLocalOf { AquaSpacing }