package com.khater.rwaq.presentation.screens.loginScreen.uiState


import com.khater.rwaq.presentation.model.SnackBarState
import com.khater.rwaq.presentation.util.LoginConstants.PASSWORD_LENGTH
import com.khater.rwaq.presentation.util.LoginConstants.PHONE_NUMBER_LENGTH
import org.jetbrains.compose.resources.StringResource

data class LoginScreenState(
    val phoneNumber: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoginButtonLoading: Boolean = false,
    val snackBar: SnackBarState = SnackBarState(),
    val phoneNumberError: StringResource? = null,
    val passwordError: StringResource? = null,
) {
    val isLoginButtonEnabled: Boolean
        get() {
            return phoneNumber.length == PHONE_NUMBER_LENGTH
        }
}
