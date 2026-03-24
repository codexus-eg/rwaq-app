package com.khater.rwaq.designSystem.component.button.content

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.khater.rwaq.designSystem.theme.theme.Theme


@Composable
internal fun BaseButtonContent(
    text: String?,
    trailingIcon: Painter?,
    contentColor: Color,
    iconSize: Dp,
    iconStartPadding: Dp,
    textDecoration: TextDecoration = TextDecoration.None,
    style: TextStyle =  Theme.typography.title.large,
    contentDescription: String? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
) {
    text?.let {
        Text(
            text = text,
            style = style,
            color = contentColor,
            overflow = overflow,
            textDecoration=textDecoration
        )
    }

    trailingIcon?.let {
        Icon(
            painter = trailingIcon,
            contentDescription = contentDescription,
            modifier = Modifier
                .padding(start = iconStartPadding)
                .size(iconSize),
            tint = Color.Unspecified
        )
    }
}