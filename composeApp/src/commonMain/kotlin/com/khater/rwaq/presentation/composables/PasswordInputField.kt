package com.khater.rwaq.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
 import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.component.textField.TextField
import com.khater.rwaq.designSystem.theme.theme.Theme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.closed_eye
import rwaq.composeapp.generated.resources.enter_your_password
import rwaq.composeapp.generated.resources.ic_lock
import rwaq.composeapp.generated.resources.open_eye
import rwaq.composeapp.generated.resources.password

@Composable
internal fun PasswordInputField(
    password: String,
    isPasswordVisible: Boolean,
    errorMessage: String? = null,
    isError:Boolean,
    onPasswordVisibilityToggled: () -> Unit,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(bottom = 4.dp),
            text = stringResource(Res.string.password),
            style = Theme.typography.title.small,
            color = Theme.colorScheme.primary.primary
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp),
            value = password,
            hint = stringResource(Res.string.enter_your_password),
            leadingIcon = painterResource(Res.drawable.ic_lock),
            onValueChanged = { onValueChange(it) },
            showTrailingDivider = false,
            errorMessage = errorMessage,
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = painterResource(
                if (isPasswordVisible) Res.drawable.open_eye
                else Res.drawable.closed_eye
            ),
            onTrailingIconClick = onPasswordVisibilityToggled
        )
    }
}