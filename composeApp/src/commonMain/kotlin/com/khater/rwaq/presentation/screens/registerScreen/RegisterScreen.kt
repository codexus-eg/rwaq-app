package com.khater.rwaq.presentation.screens.registerScreen

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
import com.khater.rwaq.presentation.composables.PasswordInputField
import com.khater.rwaq.presentation.composables.PhoneNumberInputField
import com.khater.rwaq.presentation.composables.RwaqBackButton
import com.khater.rwaq.presentation.composables.RwaqTopBar
import com.khater.rwaq.presentation.navigation.Screen.OTPScreen
import com.khater.rwaq.presentation.screens.registerScreen.component.UsernameInputField
import com.khater.rwaq.presentation.screens.registerScreen.uiState.RegisterInteractionListener
import com.khater.rwaq.presentation.screens.registerScreen.uiState.RegisterUiEffect
import com.khater.rwaq.presentation.screens.registerScreen.uiState.RegisterUiState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.create_new_account
import rwaq.composeapp.generated.resources.next
import rwaq.composeapp.generated.resources.welcome

@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel = koinViewModel(),
) {
    val state = registerViewModel.state.collectAsStateWithLifecycle().value

    EventHandler(registerViewModel.effect) { effect, navController ->
        when (effect) {
            is RegisterUiEffect.NavigateToOTP -> {
                navController.navigate(
                    OTPScreen(
                        phoneNumber = effect.phoneNumber,
                        isRegister = true
                    )
                )
            }

            RegisterUiEffect.NavigateBack -> {
                navController.navigateUp()
            }
        }
    }

    RegisterContent(
        state = state, interactionListener = registerViewModel as RegisterInteractionListener
    )


}

@Composable
fun RegisterContent(
    state: RegisterUiState,
    interactionListener: RegisterInteractionListener,
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

        }) {


        AuthHeader(
            title = stringResource(Res.string.create_new_account),
            body = stringResource(Res.string.welcome),
            modifier = Modifier
                .padding(bottom = Theme.spacing._24)
        )
        PhoneNumberInputField(
            phoneNumber = state.phoneNumber,
            onPhoneNumberChange = interactionListener::onPhoneNumberChanged,
            isError = state.phoneNumberError != null,
            errorMessage = state.phoneNumberError?.let {
                stringResource(it)
            })
        UsernameInputField(
            modifier = Modifier.padding(top = Theme.spacing._12),
            username = state.username,
            onValueChange = interactionListener::onUsernameChanged,
            isError = state.usernameError != null,
            errorMessage = state.usernameError?.let {
                stringResource(it)
            })
        PasswordInputField(
            modifier = Modifier.padding(top = Theme.spacing._12),
            password = state.password,
            isPasswordVisible = state.isPasswordVisible,
            onPasswordVisibilityToggled = interactionListener::onPasswordVisibilityToggled,
            onValueChange = interactionListener::onPasswordChanged,
            isError = state.passwordError != null,
            errorMessage = state.passwordError?.let {
                stringResource(it)
            })
        PasswordInputField(
            modifier = Modifier.padding(top = Theme.spacing._12),
            password = state.confirmPassword,
            isPasswordVisible = state.isConfirmPasswordVisible,
            onPasswordVisibilityToggled = interactionListener::onConfirmPasswordVisibilityToggled,
            onValueChange = interactionListener::onConfirmPasswordChanged,
            isError = state.confirmPasswordError != null,
            errorMessage = state.confirmPasswordError?.let {
                stringResource(it)
            })
        Spacer(modifier = Modifier.weight(1f))
        PrimaryButton(
            modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
                .padding(vertical = Theme.spacing._16).imePadding(),
            text = stringResource(Res.string.next),
            onClick = {
                interactionListener.onClickNext(
                    phoneNumber = state.phoneNumber,
                    username = state.username,
                    password = state.password,
                    isAquaClient = state.isAquaClient
                )
            },
            isLoading = state.isNextButtonLoading,
            isEnabled = state.isNextButtonEnabled,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 13.dp)
        )

    }

}