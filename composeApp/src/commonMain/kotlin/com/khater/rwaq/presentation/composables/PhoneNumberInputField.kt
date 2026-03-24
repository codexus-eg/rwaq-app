package com.khater.rwaq.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.khater.rwaq.designSystem.component.textField.MobileNumberTextField
import com.khater.rwaq.designSystem.theme.theme.Theme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.enter_your_phone_number
import rwaq.composeapp.generated.resources.phone_icon
import rwaq.composeapp.generated.resources.phone_number

@Composable
internal fun PhoneNumberInputField(
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    sectionHeader: String = stringResource(Res.string.phone_number),
    errorMessage: String? = null,
    leadingIcon: Painter? = painterResource(Res.drawable.phone_icon),
    isError:Boolean,
    readOnly:Boolean = false,
    isFocused:Boolean = false,
    phoneNumberFilter: (String) -> String = { it.filter { char -> char.isDigit() } }
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = sectionHeader,
            style = Theme.typography.title.small,
            color = Theme.colorScheme.primary.primary
        )

        MobileNumberTextField(
            value = phoneNumber,
            errorMessage = errorMessage,
            isError = isError,
             leadingIcon = leadingIcon,
            readOnly = readOnly,
            onFocusChanged = {isFocused

            },
             onValueChanged = { newValue ->
                val filtered = phoneNumberFilter(newValue)
                onPhoneNumberChange(filtered)
            },
            hint = stringResource(Res.string.enter_your_phone_number),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
         )
    }
}

