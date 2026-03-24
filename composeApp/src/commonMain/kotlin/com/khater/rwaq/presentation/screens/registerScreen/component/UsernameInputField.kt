package com.khater.rwaq.presentation.screens.registerScreen.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
 import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.component.textField.TextField
import com.khater.rwaq.designSystem.theme.theme.Theme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.enter_full_name
import rwaq.composeapp.generated.resources.full_name
import rwaq.composeapp.generated.resources.user

@Composable
internal fun UsernameInputField(
    username: String,
    errorMessage: String? = null,
    isError:Boolean,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(bottom = 4.dp),
            text = stringResource(Res.string.full_name),
            style = Theme.typography.title.small,
            color = Theme.colorScheme.primary.primary
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp),
            value = username,
            hint = stringResource(Res.string.enter_full_name),
            leadingIcon = painterResource(Res.drawable.user),
            onValueChanged = { onValueChange(it) },
            showTrailingDivider = false,
            errorMessage = errorMessage,
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
         )
    }
}