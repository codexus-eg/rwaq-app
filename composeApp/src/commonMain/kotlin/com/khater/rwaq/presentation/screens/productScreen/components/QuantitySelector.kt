package com.khater.rwaq.presentation.screens.productScreen.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.component.text.Text
import com.khater.rwaq.designSystem.theme.theme.Theme
import org.jetbrains.compose.resources.painterResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.minus
import rwaq.composeapp.generated.resources.plus

@Composable
fun QuantitySelector(
    count: Int,
    maxCount: Int = Int.MAX_VALUE,
    minCount: Int = 1,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    isBig: Boolean = false,
) {
     val height = if (isBig) 50.dp else 40.dp

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .height(height)
            .background(Color(0xFFF5F5F5), CircleShape)
            .padding(horizontal = 4.dp)
            .animateContentSize()
    ) {

            com.khater.rwaq.designSystem.component.icon.IconButton(
                painter = painterResource(Res.drawable.minus),
                contentDescription = "Minus amount",
                tint = if (count > minCount) Theme.colorScheme.primary.primary
                else Theme.colorScheme.shadePrimary,
                onClick = { if (count > minCount) onDecrease() },
                modifier = Modifier.size(32.dp).clip(CircleShape)
            )

        Text(
            text = "$count",
            modifier = Modifier.padding(horizontal = 8.dp),
            style = Theme.typography.title.large,
            color = Theme.colorScheme.primary.primary,
            fontWeight = FontWeight.Bold
        )

        com.khater.rwaq.designSystem.component.icon.IconButton(
            painter = painterResource(Res.drawable.plus),
            contentDescription = "Plus amount",
            tint = if (count < maxCount) Theme.colorScheme.primary.primary
                else Theme.colorScheme.shadePrimary,
            onClick = { if (count < maxCount) onIncrease() },
            modifier = Modifier.size(32.dp).clip(CircleShape)
        )

    }
}
