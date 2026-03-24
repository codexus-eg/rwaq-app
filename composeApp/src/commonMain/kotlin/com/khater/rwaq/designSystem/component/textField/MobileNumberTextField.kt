package com.khater.rwaq.designSystem.component.textField

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.VisualTransformation
import com.khater.rwaq.designSystem.theme.theme.Theme

@Composable
fun MobileNumberTextField(
    value: String,
    hint: String,
    leadingIcon: Painter?,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingContent: (@Composable () -> Unit)? = null,
    maxCharacters: Int = 16,
    title: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    leadingIconTint: Color = Theme.colorScheme.shadePrimary,
    shape: Shape = CircleShape,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    focusRequester: FocusRequester = FocusRequester(),
    onFocusChanged: (Boolean) -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    BasicTextField(
        value = value,
        onValueChanged = onValueChanged,
        hint = hint,
        leadingIcon = leadingIcon,
        trailingIcon = null,
        title = title,
        leadingContent = leadingContent,
        leadingIconTint = leadingIconTint,
        enabled = enabled,
        readOnly = readOnly,
        isError = isError,
        errorMessage = errorMessage,
        shape = shape,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        focusRequester = focusRequester,
        onFocusChanged = onFocusChanged,
        visualTransformation = visualTransformation,
        modifier = modifier,
        maxCharacters = maxCharacters,
    )
}

