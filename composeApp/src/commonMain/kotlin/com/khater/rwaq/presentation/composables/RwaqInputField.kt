package com.khater.rwaq.presentation.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khater.rwaq.presentation.util.empty
import org.jetbrains.compose.resources.Font
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.cairo_regular
import rwaq.composeapp.generated.resources.noto_kufi_arabic_regular


@Composable
fun RwaqInputField(
    text: String,
    hint: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActions: KeyboardActions = KeyboardActions(),
    imeAction: ImeAction = ImeAction.Default,
    error: String? = null,
    readOnly: Boolean =false,
    isErrorVisible: Boolean = false,
    hintColor: Color = Color(0xFF464646),
    textColor: Color = Color(0xFF464646),
    textSize: TextUnit = 16.sp,
    fontFamily: FontFamily = FontFamily(Font(Res.font.noto_kufi_arabic_regular)),
    fontWeight: FontWeight = FontWeight.W600,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    focusedContainerColor: Color = Color.Transparent,
    unfocusedContainerColor: Color = Color.Transparent,
    borderColor: Color = Color(0xFFBDBDBD),
    shape: Shape = RoundedCornerShape(12.dp),
    onValueChange: (String) -> Unit,
) {

    Column {
        var isFocused = remember { mutableStateOf(false) }

        Box(
            modifier = modifier
                .height(62.dp)
                .border(1.dp, borderColor, shape)
                .background(
                    if (isFocused.value) focusedContainerColor else unfocusedContainerColor,
                    shape
                )
                .padding(horizontal = 12.dp, vertical = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                // Leading icon
                if (leadingIcon != null) {
                    Box(
                        modifier = Modifier.padding(end = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        leadingIcon()
                    }
                }

                // TextField content
                Box(modifier = Modifier.weight(1f)) {
                    BasicTextField(
                        value = text,
                        onValueChange = onValueChange,
                        singleLine = true,
                        readOnly = readOnly,
                        textStyle = TextStyle(
                            fontFamily = fontFamily,
                            fontSize = textSize,
                            fontWeight = fontWeight,
                            textAlign = TextAlign.Start,
                            color = textColor
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = keyboardType,
                            imeAction = imeAction
                        ),
                        keyboardActions = keyboardActions,
                        cursorBrush = SolidColor(textColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { isFocused.value = it.isFocused }
                    )

                    // Hint
                    if (text.isEmpty()) {
                        Text(
                            text = hint,
                            color = hintColor,
                            fontFamily = FontFamily(Font(Res.font.noto_kufi_arabic_regular)),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W400
                        )
                    }
                }

                // Trailing icon
                if (trailingIcon != null) {
                    Box(
                        modifier = Modifier.padding(start = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        trailingIcon()
                    }
                }
            }
        }

        // Error text
        AnimatedVisibility(isErrorVisible) {
            Text(
                text = error ?: String.empty,
                modifier = Modifier.padding(start = 16.dp, top = 3.dp),
                fontFamily = FontFamily(Font(Res.font.cairo_regular)),
                fontWeight = FontWeight.SemiBold,
                fontSize = 10.sp,
                color = Red
            )
        }
    }
}
