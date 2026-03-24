package com.khater.rwaq.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.khater.rwaq.designSystem.theme.theme.Theme

@Composable
  fun AuthHeader(
    title: String,
    body: String,
    modifier: Modifier = Modifier) {

    Column(
        modifier = modifier.fillMaxWidth(),
     ) {
        Text(
            text = title,
            textAlign = TextAlign.Center,
            style = Theme.typography.headline.large,
            color = Theme.colorScheme.primary.primary
        )
        Text(
            modifier = Modifier.padding(top = Theme.spacing._8),
            text = body,
            style = Theme.typography.body.small,
            color = Theme.colorScheme.secondary.secondary,
        )

    }
}