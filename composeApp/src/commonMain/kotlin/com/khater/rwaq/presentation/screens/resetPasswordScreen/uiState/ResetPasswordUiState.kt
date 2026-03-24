package com.khater.rwaq.presentation.screens.resetPasswordScreen.uiState


import com.khater.rwaq.presentation.model.SnackBarState
import com.khater.rwaq.presentation.util.LoginConstants.PASSWORD_LENGTH
import org.jetbrains.compose.resources.StringResource

data class ResetPasswordUiState(
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val snackBar: SnackBarState = SnackBarState(),
    val isConfirmButtonLoading: Boolean = false,
    val passwordError: StringResource? = null,
) {
    val isConfirmButtonEnabled: Boolean
        get() {
            return password.length >= PASSWORD_LENGTH
        }
}
