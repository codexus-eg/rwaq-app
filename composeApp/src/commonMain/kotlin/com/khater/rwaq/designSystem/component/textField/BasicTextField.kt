package com.khater.rwaq.designSystem.component.textField

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.theme.theme.Theme

@Composable
fun BasicTextField(
    value: String,
    hint: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingContent: @Composable (() -> Unit)? = null,
    leadingIcon: Painter? = null,
    trailingIcon: Painter? = null,
    title: String? = null,
    leadingIconTint: Color = Theme.colorScheme.shadePrimary,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textFieldHeight:Dp = 56.dp,
    isError: Boolean = false,
    showTrailingDivider: Boolean = true,
    errorMessage: String? = null,
    shape: Shape = CircleShape,
    containerColor: Color = Theme.colorScheme.brand.onBrand,
     keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    focusRequester: FocusRequester = FocusRequester(),
    onFocusChanged: (Boolean) -> Unit = {},
    onTrailingIconClick: (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    maxCharacters: Int = Int.MAX_VALUE,
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(modifier) {
        title?.let {
            Text(
                text = title,
                style = Theme.typography.title.small,
                modifier = Modifier.padding(bottom = 4.dp),
                color = Theme.colorScheme.primary.primary,
            )
        }

        Row(Modifier.fillMaxWidth()) {
            leadingContent?.let {
                leadingContent()
                Spacer(Modifier.width(Theme.spacing._4))
            }

            BasicTextField(
                value = value,
                onValueChange = {
                    if (it.length <= maxCharacters)
                        onValueChanged(it)
                },
                enabled = enabled,
                readOnly = readOnly,
                minLines = minLines,
                maxLines = if (singleLine) 1 else maxLines,
                textStyle = Theme.typography.title.medium,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                singleLine = singleLine,
                cursorBrush = SolidColor(Theme.colorScheme.primary.primary),
                decorationBox = { innerTextField ->
                    TextFieldContent(
                        innerTextField = innerTextField,
                        text = value,
                        isError = isError,
                        isFocused = isFocused,
                        singleLine = singleLine,
                        hint = hint,
                        leadingIcon = leadingIcon,
                        shape = shape,
                        trailingIcon = trailingIcon,
                        onTrailingIconClick = onTrailingIconClick,
                        showTrailingDivider = showTrailingDivider,
                        leadingIconTint = leadingIconTint
                    )
                },
                visualTransformation = visualTransformation,
                modifier = Modifier
                    .weight(1f)
                    .height(textFieldHeight)
                    .clip(shape)
                    .background(color = containerColor)
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        isFocused = it.isFocused
                        onFocusChanged(it.isFocused)
                    }
            )
        }

        AnimatedVisibility(visible = errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                style = Theme.typography.body.small,
                modifier = Modifier.padding(
                    start = 12.dp,
                    top = 2.dp
                ),
                color = Theme.colorScheme.error
            )
        }
    }
}

@Composable
private fun TextFieldContent(
    innerTextField: @Composable () -> Unit,
    text: String,
    hint: String,
    shape: Shape,
    leadingIcon: Painter?,
    trailingIcon: Painter?,
    leadingIconTint: Color,
    isError: Boolean,
    isFocused: Boolean,
    singleLine: Boolean,
    showTrailingDivider: Boolean = true,
    onTrailingIconClick: (() -> Unit)? = null,
) {

    val animatedIconErrorColor by animateColorAsState(
        targetValue = if (isError) Theme.colorScheme.error else leadingIconTint,
        label = "iconColorAnimation"
    )
    val borderColor = when {
        isFocused -> if (!isError)  Theme.colorScheme.brand.brand else Theme.colorScheme.border.error
        isError -> Theme.colorScheme.border.error
        else -> Theme.colorScheme.shadePrimary.copy(0.1f)
    }

     val animatedBorderColor by animateColorAsState(
        targetValue = borderColor,
        animationSpec = tween(500),
        label = "borderColorAnimation"
    )

    Row(
        modifier = Modifier.border(1.dp,animatedBorderColor,shape).padding(12.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingIcon?.let {

            Icon(
                painter = leadingIcon,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = Theme.spacing._8)
                    .size(24.dp),
                tint = animatedIconErrorColor
            )
        }

        InnerTextFieldWithHint(
            innerTextField = innerTextField,
            text = text,
            hint = hint,
            singleLine = singleLine,
            modifier = Modifier.weight(1f)
        )

        trailingIcon?.let {
            if (showTrailingDivider)
                VerticalDivider()
            Image(
                painter = trailingIcon,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        enabled = onTrailingIconClick != null,
                        indication = null,
                        interactionSource = remember {
                            MutableInteractionSource()
                        }
                    ) {
                        onTrailingIconClick?.invoke()
                    }
            )
        }
    }

}

@Composable
private fun InnerTextFieldWithHint(
    innerTextField: @Composable (() -> Unit),
    text: String,
    hint: String,
    singleLine: Boolean,
    modifier: Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart
    ) {
        innerTextField()
        if (text.isEmpty()) {
            Text(
                text = hint,
                style = Theme.typography.title.medium,
                color = Theme.colorScheme.secondary.secondary
            )
        }
    }
}

@Composable
private fun VerticalDivider() {
    Box(
        Modifier
            .padding(horizontal = 8.dp)
            .size(1.dp, 21.dp)
            .background(Theme.colorScheme.stroke),
    )
}
