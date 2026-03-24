package com.khater.rwaq.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.component.button.PrimaryButton
import com.khater.rwaq.designSystem.theme.theme.Theme
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.retry

@Composable
fun EmptyOrErrorContent(
    painter: Painter,
    message:String,
    imageSize: Dp = 97.dp,
    isError: Boolean = false,
    onRetry: () -> Unit = {},
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.fillMaxWidth() ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            Theme.spacing._24,
            Alignment.CenterVertically
        )
    ) {
        Image(
            painter = painter,
            contentDescription = "Something went wrong",
            modifier = Modifier.height(imageSize),
        )
        Text(
            text = message,
            color = Theme.colorScheme.primary.primary,
            style = Theme.typography.body.medium
        )
        if (isError)
       PrimaryButton(
           text = stringResource(Res.string.retry),
           onClick = onRetry,
           modifier = Modifier.height(40.dp),
        )
    }
}