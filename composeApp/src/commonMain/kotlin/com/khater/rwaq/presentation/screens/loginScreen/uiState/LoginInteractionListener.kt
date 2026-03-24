package com.khater.rwaq.presentation.screens.loginScreen.uiState

interface LoginInteractionListener {
    fun onPhoneNumberChanged(phoneNumber: String)
    fun onPasswordChanged(password: String)
    fun onPasswordVisibilityToggled()
    fun onClickLogin()
     fun onClickForgetPassword()
    fun onClickBack()

}