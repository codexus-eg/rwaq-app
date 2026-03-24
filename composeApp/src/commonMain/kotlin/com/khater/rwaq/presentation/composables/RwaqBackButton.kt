package com.khater.rwaq.presentation.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.adamglin.composeshadow.dropShadow
import com.khater.rwaq.designSystem.component.icon.IconButton
import com.khater.rwaq.designSystem.theme.theme.Theme
import org.jetbrains.compose.resources.painterResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.arrow_right

@Composable
fun RwaqBackButton(
    painter: Painter = painterResource(Res.drawable.arrow_right),
    modifier: Modifier = Modifier,
    hasDropShadow: Boolean = true,
    width: Dp = 50.dp,
    tint:Color = Theme.colorScheme.primary.primary,
    onClick: () -> Unit = {}) {
    val layoutDirection = LocalLayoutDirection.current

    IconButton(
        painter = painter,
        contentDescription = "Back",
        shape = RoundedCornerShape(Theme.spacing._8),
        tint = tint,
        containerColor = if(hasDropShadow) Theme.colorScheme.brand.onBrand else Theme.colorScheme.brand.onBrand.copy(0.3f),
        modifier = modifier
            //.padding(top = Theme.spacing._8, bottom = Theme.spacing._4)
            .size(45.dp)
            .width(width)
            .then(
                if (hasDropShadow){
                    Modifier .dropShadow(
                        shape = RoundedCornerShape(Theme.spacing._8),
                        color = Color.Black.copy(0.2f),
                        blur = 4.dp,
                        offsetY = 0.5.dp
                    )
                } else Modifier
            )
           .graphicsLayer {
                if (layoutDirection == LayoutDirection.Ltr) {
                    scaleX = -1f
                }
            },
        onClick = onClick
    )
}