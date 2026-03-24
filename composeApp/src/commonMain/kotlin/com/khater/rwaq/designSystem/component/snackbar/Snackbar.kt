package com.khater.rwaq.designSystem.component.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khater.rwaq.designSystem.theme.theme.Theme

@Composable
fun SnackBar(
    title: String,
    message: String,
    leadingIcon: Painter,
    modifier: Modifier = Modifier,
    containerColor: Color,
    tint: Color = Color.Unspecified,
    contentDescription: String? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing._8),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Theme.radius.md))
            .background(Theme.colorScheme.background.surfaceLow)
            .padding(
                top = Theme.spacing._8,
                bottom = Theme.spacing._8,
                start = Theme.spacing._12,
                end = Theme.spacing._24
            )
    ) {
        Icon(
            painter = leadingIcon,
            contentDescription = contentDescription,
            modifier = Modifier.size(28.dp),
            tint = tint
        )

        Column {
            Text(
                text = title,
                style = Theme.typography.label.large,
                color = Theme.colorScheme.brand.onBrand,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )

            Text(
                text = message,
                style = Theme.typography.body.small,
                color = Theme.colorScheme.brand.onBrand
            )
        }
    }
}
