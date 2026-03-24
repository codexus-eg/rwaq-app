package com.khater.rwaq.presentation.screens.loginScreen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khater.rwaq.presentation.composables.RwaqInputField
import com.khater.rwaq.presentation.util.LoginConstants.PHONE_NUMBER_LENGTH
import com.khater.rwaq.presentation.util.LoginConstants.USERNAME_MAX_LENGTH
import com.khater.rwaq.presentation.util.empty
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.country_code
import rwaq.composeapp.generated.resources.country_name
import rwaq.composeapp.generated.resources.noto_kufi_arabic_regular
import rwaq.composeapp.generated.resources.phone_number
import rwaq.composeapp.generated.resources.username

@Composable
fun PhoneNumberSection(
    phoneNumber: String,
    onPhoneNumberChange: (phoneNumber: String) -> Unit,
    username: String,
    onUsernameChange: (userName: String) -> Unit,
    phoneNumberErrorMessage: String?,
    usernameErrorMessage: String?,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, color = Color(0xFF9ac8df), shape = RoundedCornerShape(12.dp))
            .padding(vertical = 16.dp, horizontal = 28.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth().height(62.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp),
                contentAlignment = Alignment.Center
            ) {
//                Image(
//                    painter = painterResource(Res.drawable.egypt_flag),
//                    contentDescription = "Country Flag",
//                    contentScale = ContentScale.Inside
//                )
            }
            Text(
                text = stringResource(Res.string.country_name),
                fontFamily = FontFamily(Font(Res.font.noto_kufi_arabic_regular)),
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                color = Color(0xFF525252)
            )
        }
        HorizontalDivider(thickness = 1.dp, color = Color(0xFFBDBDBD))

        RwaqInputField(
            text = username,
            hint = stringResource(Res.string.username),
            onValueChange = { value ->
                if (value.length <= USERNAME_MAX_LENGTH)
                    onUsernameChange(value.filter { !it.isDigit() })
            },
            keyboardType = KeyboardType.Text,
            leadingIcon = {
                TextFieldLeadingIcon(content = {
//                    Icon(
//                        painterResource(Res.drawable.fill_profile),
//                        contentDescription = "User name",
//                        modifier = Modifier.size(25.dp).width(30.dp),
//                        tint = Color.Gray
//                    )
                })

            },
            imeAction = ImeAction.Done,
            borderColor = Color.Transparent,
            keyboardActions = KeyboardActions(onDone = { onUsernameChange(username) }),
            isErrorVisible = usernameErrorMessage != String.empty,
            error = usernameErrorMessage,
        )
        HorizontalDivider(thickness = 1.dp, color = Color(0xFFBDBDBD))

        RwaqInputField(
            text = phoneNumber,
            hint = stringResource(Res.string.phone_number),
            onValueChange = { value ->
                if (value.length <= PHONE_NUMBER_LENGTH)
                    onPhoneNumberChange(value.filter { it.isDigit() }.trim())
            },
            keyboardType = KeyboardType.Number,
            leadingIcon = {
                TextFieldLeadingIcon(content = {
                    Text(
                        text = stringResource(Res.string.country_code),
                        fontFamily = FontFamily(Font(Res.font.noto_kufi_arabic_regular)),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500,
                        color = Color(0xFF464646),
                        modifier = Modifier.width(30.dp)
                    )
                })
            },
            imeAction = ImeAction.Done,
            borderColor = Color.Transparent,
            keyboardActions = KeyboardActions(onDone = { onPhoneNumberChange(phoneNumber) }),
            isErrorVisible = phoneNumberErrorMessage != String.empty,
            error = phoneNumberErrorMessage,
        )

    }
}

@Composable
fun TextFieldLeadingIcon(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        content()
        Box(
            modifier = Modifier
                .height(30.dp)
                .width(1.dp)
                .background(Color(0xFF3D3D3D)),
        )
    }
}