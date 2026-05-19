package com.khater.rwaq.presentation.screens.loginScreen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khater.rwaq.presentation.util.empty
import org.jetbrains.compose.resources.Font
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.noto_kufi_arabic_regular

@Composable
fun OTPViewRoundedCorner(
    modifier: Modifier = Modifier,
    otpText: String,
    spacedBy: Dp = 8.dp,
    showOtp: Boolean = false,
    otpCount: Int = 6,
    boxBorderWidth: Dp = 1.dp,
    boxSize: Dp = 40.dp,
    showError: Boolean = false,
    textColor: Color = Color(0xFF464646),
    textSize: TextUnit = 16.sp,
    backgroundColor: Color = Color.White,
    focusedBoxColor: Color = Color(0xFF234E69),
    errorBoxColor: Color = Color.Red,
    unFocusedBoxColor: Color = Color(0xFFBDBDBD),
    shape: Shape = RoundedCornerShape(12.dp),
    onOtpTextChange: (String) -> Unit,
) {
    val focus = LocalFocusManager.current
    BasicTextField(
        modifier = modifier,
        value = TextFieldValue(
            text = otpText,
            selection = TextRange(otpText.length)
        ),
        onValueChange = {
            if (it.text.length <= otpCount) {
                onOtpTextChange.invoke(it.text.filter { it.isDigit() }.trim())
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Done
        ),
        decorationBox = {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Row(
                    horizontalArrangement = Arrangement
                        .spacedBy(spacedBy, Alignment.CenterHorizontally)
                ) {
                    repeat(otpCount) { index ->
                        OtpView(
                            showOtp = showOtp,
                            index = index,
                            shape = shape,
                            showError = showError,
                            text = otpText,
                            textColor = textColor,
                            textSize = textSize,
                            boxBorderWidth = boxBorderWidth,
                            boxSize = boxSize,
                            backgroundColor = backgroundColor,
                            focusedBoxColor = focusedBoxColor,
                            errorBoxColor = errorBoxColor,
                            unFocusedBoxColor = unFocusedBoxColor
                        )
                    }
                }
            }
        },
        keyboardActions = KeyboardActions(
            onDone = {
                focus.clearFocus()
            }
        )
    )
}

@Composable
fun OtpView(
    index: Int,
    showError: Boolean = false,
    text: String,
    showOtp: Boolean = false,
    shape: Shape,
    textColor: Color,
    textSize: TextUnit,
    boxBorderWidth: Dp,
    boxSize: Dp,
    backgroundColor: Color,
    focusedBoxColor: Color,
    errorBoxColor: Color,
    unFocusedBoxColor: Color,
) {
    val isFocused = text.length == index
    var char = when {
        index == text.length -> String.empty
        index > text.length -> String.empty
        else -> text[index].toString()
    }

    Box(
        modifier = Modifier
           .size(boxSize)
            .border(
                border = BorderStroke(
                    width = boxBorderWidth,
                    color = if (showError) {
                        errorBoxColor
                    } else if (isFocused) {
                        focusedBoxColor
                    } else {
                        unFocusedBoxColor
                    }
                ),
                shape = shape
            )
            .background(
                color = backgroundColor,
                shape = shape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Center),
            text = char,
            fontSize = textSize,
            maxLines = 1,
            color = textColor,
            fontFamily = FontFamily(Font(Res.font.noto_kufi_arabic_regular)),
            fontWeight = FontWeight.W500,
            textAlign = TextAlign.Center
        )
    }
}
