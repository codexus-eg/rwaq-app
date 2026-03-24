package com.khater.rwaq.designSystem.theme.typography

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle

@Immutable
data class Typography(
    val headline: Headline,
    val title: Title,
    val body: Body,
     val label: Label
) {

    data class Headline(
        val large: TextStyle,
        val medium: TextStyle,
        val small: TextStyle
    )

    data class Title(
        val large: TextStyle,
        val medium: TextStyle,
        val small: TextStyle
    )

    data class Body(
        val large: TextStyle,
        val medium: TextStyle,
        val small: TextStyle,
        val extraSmall: TextStyle
    )

    data class Label(
        val large: TextStyle,
        val medium: TextStyle,
        val small: TextStyle,
        val extraSmall: TextStyle
    )
}

internal val LocalTypography =
    staticCompositionLocalOf<Typography> { error("No Typography provided") }