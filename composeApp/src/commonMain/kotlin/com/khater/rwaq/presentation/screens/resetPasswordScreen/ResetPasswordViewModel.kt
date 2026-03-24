package com.khater.rwaq.presentation.screens.resetPasswordScreen

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.khater.rwaq.domain.useCases.auth.ResetPasswordUseCase
import com.khater.rwaq.presentation.base.BaseViewModel
import com.khater.rwaq.presentation.mapper.handleAuthException
import com.khater.rwaq.presentation.mapper.mapAuthenticationErrorToMessage
import com.khater.rwaq.presentation.model.SnackBarState
import com.khater.rwaq.presentation.navigation.Screen
import com.khater.rwaq.presentation.screens.resetPasswordScreen.uiState.ResetPasswordInteractionListener
import com.khater.rwaq.presentation.screens.resetPasswordScreen.uiState.ResetPasswordUiEffect
import com.khater.rwaq.presentation.screens.resetPasswordScreen.uiState.ResetPasswordUiState
import com.khater.rwaq.presentation.util.LoginConstants.PASSWORD_LENGTH
import com.khater.rwaq.presentation.util.LoginConstants.PASSWORD_MAX_LENGTH
import com.khater.rwaq.presentation.util.LoginConstants.SNACK_BAR_DELAY
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.enter_valid_password

class ResetPasswordViewModel(
    savedStateHandle: SavedStateHandle,
    private val resetPasswordUseCase: ResetPasswordUseCase,
) :
    BaseViewModel<ResetPasswordUiState, ResetPasswordUiEffect>(ResetPasswordUiState()),
    ResetPasswordInteractionListener {

    val args = savedStateHandle.toRoute<Screen.ResetPasswordScreen>()

    override fun onClickBack() {
        sendNewEffect(newEffect = ResetPasswordUiEffect.NavigateBack)
    }

    override fun onPasswordVisibilityToggled() {
        updateState { it.copy(isPasswordVisible = !state.value.isPasswordVisible) }
    }

    override fun onPasswordChanged(password: String) {
        password.takeIf { it.length < PASSWORD_MAX_LENGTH }
            ?.let { newPassword ->
                updateState { it.copy(password = newPassword, passwordError = null) }
            }
    }

    override fun onClickConfirm() {
        if (validateInputs()) {
            tryToExecute(
                callee = {
                    resetPasswordUseCase.resetPassword(
                        phoneNumber = args.phoneNumber,
                        newPassword = currentState.password,
                        otp = args.otp
                    )
                },
                onStart = { updateState { it.copy(isConfirmButtonLoading = true) } },
                onSuccess = { onResetPasswordSuccess() },
                onError = ::onConfirmNewPasswordError
            )
        }
    }

    private suspend fun onConfirmNewPasswordError(throwable: Throwable) {
        updateState { it.copy(isConfirmButtonLoading = false) }
        showSnackBar(message = getString(mapErrorMessage(throwable)), isSuccess = false)
    }

    private fun mapErrorMessage(throwable: Throwable): StringResource {
        val errorState = handleAuthException(throwable)
        return mapAuthenticationErrorToMessage(errorState)
    }

    private suspend fun showSnackBar(
        title: String? = null,
        message: String,
        isSuccess: Boolean,
        durationMillis: Long = SNACK_BAR_DELAY,
    ) {
        updateState { oldState ->
            oldState.copy(
                snackBar = SnackBarState(
                    isVisible = true,
                    title = title,
                    message = message,
                    isSuccess = isSuccess
                )
            )
        }

        delay(durationMillis)

        hideSnackBar()
    }

    private fun hideSnackBar() {
        updateState { oldState ->
            oldState.copy(snackBar = oldState.snackBar.copy(isVisible = false))
        }
    }

    private fun onResetPasswordSuccess() {
        updateState { it.copy(isConfirmButtonLoading = false) }
        sendNewEffect(newEffect = ResetPasswordUiEffect.NavigateToHome)
    }

    private fun validateInputs(): Boolean {
        val password = currentState.password

        val isPasswordContainsDigits = password.any { it.isDigit() }
        val isPasswordContainsChars = password.any { it.isLetter() }
        val isPasswordComplex =
            isPasswordContainsChars && isPasswordContainsDigits && password.length >= PASSWORD_LENGTH

        updateState {
            it.copy(
                passwordError = if (!isPasswordComplex) Res.string.enter_valid_password else null,
            )
        }

        return isPasswordComplex
    }
}