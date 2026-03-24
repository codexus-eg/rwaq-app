package com.khater.rwaq.presentation.composables

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.theme.theme.Theme

//
//@Composable
//internal fun OtpInput(
//    otpValue: String,
//    onOtpChange: (String) -> Unit,
//    modifier: Modifier = Modifier,
//    otpLength: Int = 4
//) {
//    var isFocused by remember { mutableStateOf(false) }
//
//    BasicTextField(
//        value = otpValue,
//        modifier = modifier
//            .onFocusChanged { focusState ->
//                isFocused = focusState.isFocused
//            },
//        onValueChange = { onOtpChange(it) },
//        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//        decorationBox = { innerTextField ->
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(
//                    space = Theme.spacing._16,
//                    alignment = Alignment.CenterHorizontally
//                ),
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                repeat(times = otpLength) { index ->
//                    val char = otpValue.getOrNull(index)?.toString().orEmpty()
//                    val showCursor = index == otpValue.length
//                    OTPCard(char = char, showCursor = showCursor, isFocused = isFocused)
//                }
//            }
//        },
//    )
//}
//
//@Composable
//private fun OTPCard(
//    char: String,
//    showCursor: Boolean = false,
//    isFocused: Boolean = false
//) {
//    val infiniteTransition = rememberInfiniteTransition(label = "cursor_blink")
//    val cursorAlpha by infiniteTransition.animateFloat(
//        initialValue = 1f,
//        targetValue = 0f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(durationMillis = 600),
//            repeatMode = RepeatMode.Reverse
//        ),
//    )
//
//    Box(
//        modifier = Modifier
//            .size(40.dp)
//            .background(
//                color = Color(0xFFFBF7F0),
//                shape = RoundedCornerShape(Theme.radius.sm)
//            ).border(1.dp, if (showCursor && isFocused) Theme.colorScheme.border.success else Color.Transparent, RoundedCornerShape(Theme.radius.sm)),
//        contentAlignment = Alignment.Center
//    ) {
//        if (char.isNotEmpty()) {
//            Text(
//                text = char,
//                style = Theme.typography.headline.medium,
//                color = Theme.colorScheme.shadePrimary
//            )
//        } else if (showCursor && isFocused) {
//            Box(
//                modifier = Modifier
//                    .size(width = 1.5.dp, height = 28.dp)
//                    .background(color = Theme.colorScheme.primary.primary.copy(alpha = cursorAlpha))
//            )
//        }
//    }
//}


@Composable
internal fun OtpInput(
    otpValue: String,
    onOtpChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    otpLength: Int = 4
) {
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = otpValue,
        modifier = modifier
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            },
        onValueChange = {
            // Limit the length to otpLength
            if (it.length <= otpLength) {
                onOtpChange(it)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = { innerTextField ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    space = Theme.spacing._16,
                    alignment = Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // We reverse the string so the "last typed" char (End of string)
                // appears in the "first box" (Index 0)
                val reversedOtp = otpValue.reversed()

                repeat(times = otpLength) { index ->
                    val char = reversedOtp.getOrNull(index)?.toString().orEmpty()

                    // The cursor always stays in the first box (Index 0)
                    // because that's where new numbers "enter" the visual stack.
                    // We also hide it if the OTP is completely full.
                    val showCursor = index == 0 && isFocused && otpValue.length < otpLength

                    OTPCard(char = char, showCursor = showCursor, isFocused = isFocused)
                }
            }
        },
    )
}

@Composable
private fun OTPCard(
    char: String,
    showCursor: Boolean = false,
    isFocused: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "cursor_blink")
    val cursorAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cursor_alpha"
    )

    Box(
        modifier = Modifier
            .size(42.dp)
            .background(
                color =Color(0xFFFBF7F0),
                shape = RoundedCornerShape(Theme.radius.sm)
            )
            .border(
                width = 1.dp,
                // Highlight border if it has a char OR if the cursor is active here
                color = if ((showCursor || char.isNotEmpty()) && isFocused) Theme.colorScheme.border.success else Color.Transparent,
                shape = RoundedCornerShape(Theme.radius.sm)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (char.isNotEmpty()) {
            Text(
                text = char,
                style = Theme.typography.headline.medium,
                color = Theme.colorScheme.shadePrimary
            )
        } else if (showCursor && isFocused) {
            Box(
                modifier = Modifier
                    .size(width = 1.5.dp, height = 28.dp)
                    .background(color = Theme.colorScheme.primary.primary.copy(alpha = cursorAlpha))
            )
        }
    }
}