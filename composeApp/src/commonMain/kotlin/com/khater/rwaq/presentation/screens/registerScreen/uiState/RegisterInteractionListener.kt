package com.khater.rwaq.presentation.screens.registerScreen.uiState

interface RegisterInteractionListener {
    fun onClickBack()
    fun onUsernameChanged(username: String)
    fun onPhoneNumberChanged(phoneNumber: String)
    fun onPasswordChanged(password: String)
    fun onConfirmPasswordChanged(password: String)
    fun onCheckIsAquaClient(isAquaClient: Boolean)
    fun onPasswordVisibilityToggled()
    fun onConfirmPasswordVisibilityToggled()
    fun onClickNext(
        phoneNumber: String,
        username: String,
        password: String,
        isAquaClient: Boolean,
    )
}