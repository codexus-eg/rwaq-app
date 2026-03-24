package com.khater.rwaq.designSystem.component.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.component.button.content.BaseButtonContent
import com.khater.rwaq.designSystem.theme.theme.Theme


@Composable
fun TextButton(
    text: String,
    onClick: () -> Unit,
    trailingIcon: Painter? = null,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    textDecoration: TextDecoration = TextDecoration.None,
    style: TextStyle = Theme.typography.title.large,
    contentColor: Color = Theme.colorScheme.brand.brand,
    disabledContentColor: Color = Theme.colorScheme.disabled,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    iconSize: Dp = 16.dp,
    iconStartPadding: Dp = Theme.spacing._4,
    overflow: TextOverflow = TextOverflow.Ellipsis
) {
    Button(
        onClick = onClick,
        isEnabled = isEnabled,
        contentPadding = contentPadding,
        contentColor = contentColor,
        disabledContentColor = disabledContentColor,
        shape = RoundedCornerShape(Theme.radius.xxs),
        isLoading = isLoading,
        loadingColors = listOf(
            Theme.colorScheme.stroke,
            Theme.colorScheme.shadeTertiary,
            Theme.colorScheme.primary.primary
        ),
        modifier = modifier
    ) {
        BaseButtonContent(
            text = text,
            trailingIcon = trailingIcon,
            iconSize = iconSize,
            style = style,
            textDecoration = textDecoration,
            iconStartPadding = iconStartPadding,
            overflow = overflow,
            contentColor = it,
        )
    }
}