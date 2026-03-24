package com.khater.rwaq.presentation.screens.productScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.khater.rwaq.designSystem.component.text.Text
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.screens.productScreen.uiState.ProductDetailsUiState
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.add
import rwaq.composeapp.generated.resources.cashback
import rwaq.composeapp.generated.resources.currency_sar

@Composable
fun ProductHeader(details: ProductDetailsUiState) {
    Logger.i { "labwlaiv $details" }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = details.name,
            style = Theme.typography.title.large,
            color = Theme.colorScheme.primary.primary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "${details.calculatedSingleUnitTestPrice} ${stringResource(Res.string.currency_sar)} ",
            style = Theme.typography.title.medium,
            color = Theme.colorScheme.primary.primary,
            fontWeight = FontWeight.Bold
        )

    }
    Spacer(modifier = Modifier.padding(top = 12.dp))
    if (details.hasCashback) {
        Text(
            text = "${stringResource(Res.string.cashback)} ${details.cashBackAmount} ${stringResource(Res.string.currency_sar)}",
            style = Theme.typography.title.medium,
            color = Color(0xFF16a085),
            fontWeight = FontWeight.Normal,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End
        )
    }
}