package com.khater.rwaq.designSystem.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.component.button.content.BaseButtonContent
import com.khater.rwaq.designSystem.theme.theme.Theme


@Composable
fun OutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingIcon: Painter? = null,
    iconSize: Dp = 20.dp,
    buttonHeight: Dp = 48.dp,
    contentDescription: String? = null,
    style: TextStyle = Theme.typography.title.large,
    iconStartPadding: Dp = Theme.spacing._8,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    contentColor: Color = Theme.colorScheme.brand.brand,
    disabledContentColor: Color = Theme.colorScheme.textDisabled,
    containerColor: Color = Theme.colorScheme.brand.onBrand,
    disabledContainerColor: Color = Theme.colorScheme.disabled,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = Theme.spacing._16,
        vertical = Theme.spacing._8
    ),
    shape: Shape = CircleShape
) {
    Button(
        isEnabled = isEnabled,
        shape = shape,
        borderStroke = BorderStroke(width = 1.dp, color = Theme.colorScheme.border.brand),
        contentColor = contentColor,
        containerColor = containerColor,
        disabledContentColor = disabledContentColor,
        disabledContainerColor = disabledContainerColor,
        contentPadding = contentPadding,
        onClick = onClick,
        isLoading = isLoading,
        loadingColors = listOf(
            Theme.colorScheme.stroke,
            Theme.colorScheme.shadeTertiary,
            Theme.colorScheme.primary.primary
        ),
        modifier = modifier.height(buttonHeight)
    ) {
        BaseButtonContent(
            text = text,
            style = style,
            trailingIcon = trailingIcon,
            iconSize = iconSize,
            iconStartPadding = iconStartPadding,
            contentDescription = contentDescription,
            contentColor = it
        )
    }
}