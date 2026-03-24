package com.khater.rwaq.presentation.screens.resetPasswordScreen

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

import com.khater.rwaq.presentation.screens.resetPasswordScreen.uiState.ResetPasswordInteractionListener
import com.khater.rwaq.presentation.screens.resetPasswordScreen.uiState.ResetPasswordUiEffect
import com.khater.rwaq.presentation.screens.resetPasswordScreen.uiState.ResetPasswordUiState
 import com.khater.rwaq.designSystem.component.button.PrimaryButton
import com.khater.rwaq.designSystem.component.scaffold.AuthScaffold
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.composables.AuthHeader
import com.khater.rwaq.presentation.composables.EventHandler
import com.khater.rwaq.presentation.composables.PasswordInputField
import com.khater.rwaq.presentation.composables.RwaqBackButton
import com.khater.rwaq.presentation.composables.RwaqTopBar
import com.khater.rwaq.presentation.navigation.Screen
import com.khater.rwaq.presentation.util.empty
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.confirm_password
import rwaq.composeapp.generated.resources.please_enter_new_password

@Composable
fun ResetPasswordScreen(
    resetPasswordViewModel: ResetPasswordViewModel = koinViewModel()
){
    val state = resetPasswordViewModel.state.collectAsStateWithLifecycle().value

    EventHandler(resetPasswordViewModel.effect) { effect,navController->
        when(effect) {
            is ResetPasswordUiEffect.NavigateBack -> navController.popBackStack()
            ResetPasswordUiEffect.NavigateToHome -> {
                navController.navigate(Screen.HomeScreen){
                    popUpTo(0)
                }
            }
        }
    }

    ResetPasswordContent(
        state = state,
        interactionListener = resetPasswordViewModel as ResetPasswordInteractionListener
    )

}

@Composable
fun ResetPasswordContent(
    state: ResetPasswordUiState,
    interactionListener: ResetPasswordInteractionListener
) {
    AuthScaffold(snackBarState = state.snackBar,
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
            title = stringResource(Res.string.please_enter_new_password),
            body = String.empty,
            modifier = Modifier.padding(bottom = Theme.spacing._24)
        )

        PasswordInputField(
            modifier = Modifier.padding(top = Theme.spacing._12),
            password = state.password,
            isPasswordVisible = state.isPasswordVisible,
            onPasswordVisibilityToggled = interactionListener::onPasswordVisibilityToggled,
            onValueChange = interactionListener::onPasswordChanged,
            isError = state.passwordError != null,
            errorMessage = state.passwordError?.let {
                stringResource(it)
            }
        )

        Spacer(modifier = Modifier.weight(1f))
        PrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(vertical = Theme.spacing._16).imePadding(),
            text = stringResource(Res.string.confirm_password),
            onClick = interactionListener::onClickConfirm,
            isLoading = state.isConfirmButtonLoading,
            isEnabled = state.isConfirmButtonEnabled,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 13.dp)
        )
    }
}