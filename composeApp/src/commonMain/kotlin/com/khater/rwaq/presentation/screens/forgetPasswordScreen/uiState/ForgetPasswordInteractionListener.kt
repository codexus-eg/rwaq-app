package com.khater.rwaq.presentation.screens.forgetPasswordScreen.uiState

interface ForgetPasswordInteractionListener {
    fun onClickBack()
    fun onPhoneNumberChanged(phoneNumber: String)
    fun onSendOtpCode()
}