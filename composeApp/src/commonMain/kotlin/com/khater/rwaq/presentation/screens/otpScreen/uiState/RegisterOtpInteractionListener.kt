package com.khater.rwaq.presentation.screens.otpScreen.uiState

interface RegisterOtpInteractionListener {
    fun onClickBack()
    fun onClickVerify()
    fun onClickResend()
    fun onChangeOtp(otp: String)
}