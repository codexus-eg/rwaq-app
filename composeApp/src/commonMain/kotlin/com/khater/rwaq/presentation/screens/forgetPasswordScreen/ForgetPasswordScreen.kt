package com.khater.rwaq.presentation.screens.forgetPasswordScreen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.khater.rwaq.designSystem.component.button.PrimaryButton
import com.khater.rwaq.designSystem.component.scaffold.AuthScaffold
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.composables.AuthHeader
import com.khater.rwaq.presentation.composables.EventHandler
import com.khater.rwaq.presentation.composables.PhoneNumberInputField
import com.khater.rwaq.presentation.composables.RwaqBackButton
import com.khater.rwaq.presentation.composables.RwaqTopBar
import com.khater.rwaq.presentation.navigation.Screen
import com.khater.rwaq.presentation.screens.forgetPasswordScreen.uiState.ForgetPasswordInteractionListener
import com.khater.rwaq.presentation.screens.forgetPasswordScreen.uiState.ForgetPasswordUiEffect
import com.khater.rwaq.presentation.screens.forgetPasswordScreen.uiState.ForgetPasswordUiState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.enter_mobile_number_message
import rwaq.composeapp.generated.resources.reset_forgot_password
import rwaq.composeapp.generated.resources.send_otp_code

@Composable
fun ForgetPasswordScreen(forgetPasswordViewModel: ForgetPasswordViewModel = koinViewModel()) {

    val state = forgetPasswordViewModel.state.collectAsStateWithLifecycle().value
    EventHandler(forgetPasswordViewModel.effect) { effect, navController ->
        when (effect) {
            ForgetPasswordUiEffect.NavigateBack -> {
                navController.navigateUp()
            }

            is ForgetPasswordUiEffect.NavigateToOtpScreen -> {
                navController.navigate(
                    Screen.OTPScreen(
                        phoneNumber = effect.phoneNumber,
                        isRegister = false
                    )
                )
            }
        }
    }

    ForgetPasswordContent(
        state,
        forgetPasswordViewModel as ForgetPasswordInteractionListener
    )
}

@Composable
fun ForgetPasswordContent(
    state: ForgetPasswordUiState,
    interactionListener: ForgetPasswordInteractionListener,
) {
    AuthScaffold(
        snackBarState = state.snackBar,
        modifier = Modifier.padding(horizontal = Theme.spacing._16),
        topBar = {
            RwaqTopBar(
                modifier = Modifier.padding(bottom = Theme.spacing._8),
                leadingContent = {
                    RwaqBackButton(
                        onClick = interactionListener::onClickBack
                    )
                }
            )

        }
    ) {

        AuthHeader(
            title = stringResource(Res.string.reset_forgot_password),
            body = stringResource(Res.string.enter_mobile_number_message),
            modifier = Modifier.padding(bottom = Theme.spacing._24)
        )
        PhoneNumberInputField(
            phoneNumber = state.phoneNumber,
            onPhoneNumberChange = interactionListener::onPhoneNumberChanged,
            isError = state.phoneNumberError != null, errorMessage = state.phoneNumberError?.let {
                stringResource(it)
            }
        )

        Spacer(modifier = Modifier.weight(1f))
        PrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(vertical = Theme.spacing._16).imePadding(),
            text = stringResource(Res.string.send_otp_code),
            onClick = interactionListener::onSendOtpCode,
            isLoading = state.isLoginButtonLoading,
            isEnabled = state.isSendOtpCodeButtonEnabled,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 13.dp)
        )
    }
}