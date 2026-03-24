package com.khater.rwaq.presentation.screens.otpScreen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.khater.rwaq.designSystem.component.button.PrimaryButton
import com.khater.rwaq.designSystem.component.scaffold.AuthScaffold
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.composables.AuthHeader
import com.khater.rwaq.presentation.composables.AuthPrompt
import com.khater.rwaq.presentation.composables.EventHandler
import com.khater.rwaq.presentation.composables.OtpInput
import com.khater.rwaq.presentation.composables.RwaqBackButton
import com.khater.rwaq.presentation.composables.RwaqTopBar
import com.khater.rwaq.presentation.navigation.Screen
import com.khater.rwaq.presentation.screens.otpScreen.uiState.RegisterOtpInteractionListener
import com.khater.rwaq.presentation.screens.otpScreen.uiState.RegisterOtpScreenState
import com.khater.rwaq.presentation.screens.otpScreen.uiState.RegisterOtpUiEffect
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.auth_code_title
import rwaq.composeapp.generated.resources.did_not_receive_code
import rwaq.composeapp.generated.resources.enter_code_message
import rwaq.composeapp.generated.resources.resend
import rwaq.composeapp.generated.resources.resend_timer
import rwaq.composeapp.generated.resources.verify

@Composable
fun RegisterOtpScreen(
    registerOtpViewModel: RegisterOtpViewModel = koinViewModel()
){
    val state = registerOtpViewModel.state.collectAsStateWithLifecycle().value

    EventHandler(registerOtpViewModel.effect) { effect, navController ->
        when(effect){
            RegisterOtpUiEffect.NavigateBack -> {
                navController.navigateUp()
            }
            RegisterOtpUiEffect.NavigateToHome -> {
                navController.navigate(Screen.HomeScreen)
            }

            is RegisterOtpUiEffect.NavigateToResetPassword -> {
                navController.navigate(Screen.ResetPasswordScreen(phoneNumber = effect.phoneNumber,otp = effect.otp))
            }
        }
    }
    RegisterOtpContent(
        state = state,
        listener = registerOtpViewModel as RegisterOtpInteractionListener
    )
}

@Composable
fun RegisterOtpContent(
    state: RegisterOtpScreenState,
    listener: RegisterOtpInteractionListener) {
    AuthScaffold(snackBarState = state.snackBar,
        modifier = Modifier.padding(horizontal = Theme.spacing._16),
        topBar = {
            RwaqTopBar(
                modifier = Modifier.padding(bottom = Theme.spacing._8),
                leadingContent = {
                    RwaqBackButton(
                        onClick = listener::onClickBack
                    )
                }
            )

        }) {


        AuthHeader(
            title = stringResource(Res.string.auth_code_title),
            body = stringResource(Res.string.enter_code_message)+"\n"+state.phoneNumber,
            modifier = Modifier
                .padding(bottom = Theme.spacing._49)
        )
        OtpInput(
            otpValue = state.otpValue,
            onOtpChange = listener::onChangeOtp,
            otpLength = 6,
        )
        val minutes = state.timer.toInt() / 60
        val seconds = (state.timer.toInt() % 60).toString().padStart(2, '0')

        AuthPrompt(
            message = stringResource(Res.string.did_not_receive_code),
            actionLabel = if (state.isResendEnabled) stringResource(Res.string.resend) else stringResource(
                Res.string.resend_timer,
                minutes,
                seconds
            ),
            onActionClick = listener::onClickResend,
            isEnabled = state.isResendEnabled,
            modifier = Modifier .fillMaxWidth()
                .padding(vertical = Theme.spacing._16)
        )

        Spacer(modifier = Modifier.weight(1f))

        PrimaryButton(
            text = stringResource(Res.string.verify),
            onClick = listener::onClickVerify,
            isEnabled = state.isVerifyEnabled,
            isLoading = state.isLoading,
            contentPadding = PaddingValues(vertical = 13.dp),
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .padding(bottom = Theme.spacing._12, top = Theme.spacing._24)
        )

    }
}