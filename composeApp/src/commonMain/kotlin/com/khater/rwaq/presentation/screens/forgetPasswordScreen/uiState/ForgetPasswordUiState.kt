package com.khater.rwaq.presentation.screens.forgetPasswordScreen.uiState

import com.khater.rwaq.presentation.model.SnackBarState
import com.khater.rwaq.presentation.util.LoginConstants.PHONE_NUMBER_LENGTH
import org.jetbrains.compose.resources.StringResource

data class ForgetPasswordUiState(
    val phoneNumber: String = "",
    val isLoginButtonLoading: Boolean = false,
    val snackBar: SnackBarState = SnackBarState(),
    val phoneNumberError: StringResource? = null,
 ){
     val isSendOtpCodeButtonEnabled: Boolean
        get() {
            return phoneNumber.length == PHONE_NUMBER_LENGTH
         }
}
