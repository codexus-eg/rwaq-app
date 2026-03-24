package com.khater.rwaq.designSystem.component.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingIcon: Painter? = null,
    iconSize: Dp = 20.dp,
    style: TextStyle = Theme.typography.title.medium,
    iconStartPadding: Dp = Theme.spacing._8,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    containerColor: Color = Theme.colorScheme.brand.brand,
    disabledContainerColor: Color = Theme.colorScheme.disabled,
    contentColor: Color = Theme.colorScheme.brand.onBrand,
    disabledContentColor: Color = Theme.colorScheme.brand.onBrand,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = Theme.spacing._16,
        vertical = Theme.spacing._8
    ),
    shape: Shape = CircleShape
) {
    Button(
        isEnabled = isEnabled,
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContentColor = disabledContentColor,
        disabledContainerColor = disabledContainerColor,
        contentPadding = contentPadding,
        shape = shape,
        isLoading = isLoading,
        loadingColors = listOf(
            Theme.colorScheme.primary.onPrimaryHint,
            Theme.colorScheme.primary.onPrimaryBody,
            Theme.colorScheme.primary.onPrimary
        ),
        onClick = onClick,
        modifier = modifier.height(56.dp)
    ) {
        BaseButtonContent(
            text = text,
            contentColor = it,
            style = style ,
            trailingIcon = trailingIcon,
            iconSize = iconSize,
            iconStartPadding = iconStartPadding
        )
    }
}
