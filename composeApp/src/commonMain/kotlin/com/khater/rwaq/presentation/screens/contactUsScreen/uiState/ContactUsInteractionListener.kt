package com.khater.rwaq.presentation.screens.contactUsScreen.uiState

interface ContactUsInteractionListener {
    fun onEmailChanged(email: String)
    fun onUsernameChanged(username: String)
    fun onMessageChanged(message: String)
    fun onSubmitClicked()
    fun onBackClicked() // For the back arrow
}