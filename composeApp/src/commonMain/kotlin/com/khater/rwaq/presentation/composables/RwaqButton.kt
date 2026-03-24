package com.khater.rwaq.presentation.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import org.jetbrains.compose.resources.Font
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.cairo_regular

@Composable
fun RwaqButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    trailingIcon: Painter? = null,
    contentColor: Color = Color.White,
    iconSize: Dp = 24.dp,
    textSize: TextUnit = 16.sp,
    borderColor: Color = Color.Transparent,
    shape: Shape = RoundedCornerShape(12.dp),
    enabledContainerBrush: Brush = Brush.linearGradient(
        listOf(
            Color(0xFF98FF98),
            Color(0xFF234E69)
        )
    ),
    disabledContainerBrush: Brush = Brush.linearGradient(
        listOf(
            Color(0xFFDCDCDC),
            Color(0xFFDCDCDC)
        )
    ),
    fontFamily: FontFamily = FontFamily(Font(Res.font.cairo_regular)),
    fontWeight: FontWeight = FontWeight.W400,
    ) {
    Box(
        modifier = modifier
            .height(46.dp)
            .clip(shape)
            .clickable(isEnabled) { onClick() }
            .background(
                if (isEnabled) enabledContainerBrush
                else disabledContainerBrush
            ).border(1.dp,borderColor, shape),
        contentAlignment = Alignment.Center
    ) {

        AnimatedVisibility(isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp))
        }

        AnimatedVisibility(isLoading.not()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = text,
                    fontSize = textSize,
                    fontFamily = fontFamily,
                    color = contentColor,
                    fontWeight = fontWeight
                )
                if (trailingIcon != null) {
                    Icon(
                        painter = trailingIcon,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }

        }
    }
}