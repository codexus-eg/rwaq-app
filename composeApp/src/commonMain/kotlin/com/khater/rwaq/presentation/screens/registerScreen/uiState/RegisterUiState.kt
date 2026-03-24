package com.khater.rwaq.presentation.screens.registerScreen.uiState


import com.khater.rwaq.presentation.model.SnackBarState
import com.khater.rwaq.presentation.util.LoginConstants.PASSWORD_LENGTH
import com.khater.rwaq.presentation.util.LoginConstants.PHONE_NUMBER_LENGTH
import com.khater.rwaq.presentation.util.LoginConstants.USERNAME_LENGTH
import org.jetbrains.compose.resources.StringResource

data class RegisterUiState(
    val phoneNumber: String = "",
    val username: String = "",
    val password: String = "",
    val isAquaClient: Boolean = false,
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isNextButtonLoading: Boolean = false,
    val snackBar: SnackBarState = SnackBarState(),
    val phoneNumberError: StringResource? = null,
    val usernameError: StringResource? = null,
    val passwordError: StringResource? = null,
    val confirmPasswordError: StringResource? = null,
 ){
     val isNextButtonEnabled: Boolean
        get() {
            return phoneNumber.length == PHONE_NUMBER_LENGTH
                    && username.length >= USERNAME_LENGTH
                    && password.length >= PASSWORD_LENGTH
                    && confirmPassword.isNotEmpty()
         }
}
