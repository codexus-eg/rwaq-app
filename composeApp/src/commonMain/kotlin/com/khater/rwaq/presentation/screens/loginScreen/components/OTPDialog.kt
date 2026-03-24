package com.khater.rwaq.presentation.screens.loginScreen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.khater.rwaq.presentation.composables.RwaqButton
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.confirm
import rwaq.composeapp.generated.resources.country_code
import rwaq.composeapp.generated.resources.enter_your_verification_code
import rwaq.composeapp.generated.resources.ic_cancle
import rwaq.composeapp.generated.resources.noto_kufi_arabic_medium
import rwaq.composeapp.generated.resources.noto_kufi_arabic_regular
import rwaq.composeapp.generated.resources.otp_receiver
import rwaq.composeapp.generated.resources.resend_code
import rwaq.composeapp.generated.resources.send_otp_to_your_phone

@Composable
fun OTPDialogWrapper(
    phoneNumber: String,
    otpCode: String,
    isEnabledToConfirmOTP: Boolean,
    timer: Int,
    onOtpCodeChange: (String) -> Unit,
    onResendCode: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss, properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(.95f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(16.dp)
        ) {

            OTPDialogContent(
                modifier = Modifier,
                phoneNumber = phoneNumber,
                onResendCode = onResendCode,
                onConfirm = onConfirm,
                otpCode = otpCode,
                onOtpCodeChange = onOtpCodeChange,
                timer = timer,
                isEnabledToConfirmOTP = isEnabledToConfirmOTP
            )
            Icon(
                painter = painterResource(Res.drawable.ic_cancle),
                contentDescription = "Dismiss Dialog",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(25.dp)
                    .clickable { onDismiss() }.padding(5.dp),
                tint = Color(0xFF464646)
            )
        }
    }
}

@Composable
fun OTPDialogContent(
    modifier: Modifier = Modifier,
    phoneNumber: String,
    otpCode: String,
    timer: Int,
    isEnabledToConfirmOTP: Boolean,
    onOtpCodeChange: (String) -> Unit,
    onResendCode: () -> Unit,
    onConfirm: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth().background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.send_otp_to_your_phone),
            fontFamily = FontFamily(Font(Res.font.noto_kufi_arabic_regular)),
            fontSize = 16.sp,
            color = Color(0xff464646),
            fontWeight = FontWeight.W400,
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "(${stringResource(Res.string.country_code)})",
                fontFamily = FontFamily(Font(Res.font.noto_kufi_arabic_regular)),
                fontSize = 12.sp,
                color = Color(0xff464646),
                fontWeight = FontWeight.W400
            )
            Text(
                text = phoneNumber,
                fontFamily = FontFamily(Font(Res.font.noto_kufi_arabic_regular)),
                fontSize = 16.sp,
                color = Color(0xff464646),
                fontWeight = FontWeight.W400
            )

        }
        Image(
            painter = painterResource(Res.drawable.otp_receiver),
            contentDescription = "OTP Receiver View",
            modifier = Modifier.height(100.dp).padding(bottom = 12.dp),
            contentScale = ContentScale.Inside
        )
        Text(
            text = stringResource(Res.string.enter_your_verification_code),
            fontFamily = FontFamily(Font(Res.font.noto_kufi_arabic_medium)),
            fontSize = 16.sp,
            color = Color(0xff464646),
            fontWeight = FontWeight.W500
        )



        OTPViewRoundedCorner(
            otpText = otpCode,
            otpCount = 6,
            showError = false,
            textSize = 16.sp,
            modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
                .padding(top = 24.dp).height(48.dp),
            onOtpTextChange = onOtpCodeChange
        )


        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "00:$timer",
                fontFamily = FontFamily(Font(Res.font.noto_kufi_arabic_regular)),
                fontSize = 16.sp,
                color = Color(0xff656565),
                fontWeight = FontWeight.W400,
            )
            Text(
                text = stringResource(Res.string.resend_code),
                fontFamily = FontFamily(Font(Res.font.noto_kufi_arabic_regular)),
                fontSize = 16.sp,
                color = Color(0xff63AACD),
                fontWeight = FontWeight.W400,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clip(CircleShape).clickable {
                    onResendCode()
                }.padding(horizontal = 15.dp)
            )


        }
        RwaqButton(
            text = stringResource(Res.string.confirm),
            onClick = onConfirm,
            isEnabled = isEnabledToConfirmOTP,
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp)
        )
    }
}

