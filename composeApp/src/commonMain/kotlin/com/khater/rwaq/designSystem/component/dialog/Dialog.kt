package com.khater.rwaq.designSystem.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.theme.theme.Theme

@Composable
fun Dialog(
    title: String,
    message: String,
    icon: Painter,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    contentDescription: String? = null,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(Theme.spacing._12))
            .background(Theme.colorScheme.brand.onBrand)
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(Theme.spacing._16),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(128.dp),
            tint = tint
        )
        Text(
            text = title,
            style = Theme.typography.title.small,
            color = Theme.colorScheme.primary.primary,
            modifier = Modifier.padding(top = Theme.spacing._16)
        )

        Text(
            text = message,
            style = Theme.typography.body.medium,
            color = Theme.colorScheme.secondary.secondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = Theme.spacing._8)

        )
    }

}
