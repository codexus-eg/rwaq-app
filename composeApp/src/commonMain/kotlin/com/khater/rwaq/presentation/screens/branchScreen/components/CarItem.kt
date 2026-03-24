package com.khater.rwaq.presentation.screens.branchScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.screens.branchScreen.uiState.CarUiState

@Composable
fun CarItem(
    modifier: Modifier = Modifier,
    car: CarUiState
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing._8)
    ) {
        AsyncImage(
            model = car.imageUrl,
            contentDescription = car.name,
            modifier = Modifier.size(Theme.spacing._32)
        )
        Box(
            modifier = Modifier.size(24.dp)
                .clip(RoundedCornerShape(Theme.spacing._8))
                .background(car.color)
        )
        Text(
            text = car.name,
            color = Theme.colorScheme.primary.primary,
            style = Theme.typography.body.small
        )
        Text(
            text = car.carNumber,
            color = Theme.colorScheme.primary.primary,
            style = Theme.typography.body.small
        )
    }

}