package com.khater.rwaq.presentation.screens.resetPasswordScreen.uiState

interface ResetPasswordInteractionListener {
    fun onClickBack()
    fun onPasswordVisibilityToggled()
    fun onPasswordChanged(password: String)
    fun onClickConfirm()
}