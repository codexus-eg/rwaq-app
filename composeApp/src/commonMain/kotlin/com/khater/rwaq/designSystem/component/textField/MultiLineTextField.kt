package com.khater.rwaq.designSystem.component.textField

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.theme.theme.Theme

@Composable
fun MultiLineTextField(
    value: String,
    hint: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    minLines: Int = 5,
    maxLines: Int = 5,
    textFieldHeight: Dp = 66.dp,
    title: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    shape: Shape = RoundedCornerShape(Theme.radius.md),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    focusRequester: FocusRequester = FocusRequester(),
    onFocusChanged: (Boolean) -> Unit = {},
) {
    BasicTextField(
        value = value,
        hint = hint,
        onValueChanged = onValueChanged,
        modifier = modifier,
        title = title,
        singleLine = false,
        minLines = minLines,
        textFieldHeight = textFieldHeight,
        maxLines = maxLines,
        enabled = enabled,
        readOnly = readOnly,
        isError = isError,
        errorMessage = errorMessage,
        shape = shape,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        focusRequester = focusRequester,
        onFocusChanged = onFocusChanged,
    )
}

