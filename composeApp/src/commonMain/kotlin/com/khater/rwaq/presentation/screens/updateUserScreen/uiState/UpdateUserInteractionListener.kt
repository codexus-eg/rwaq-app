package com.khater.rwaq.presentation.screens.updateUserScreen.uiState

interface UpdateUserInteractionListener {
    fun onEmailChanged(email: String)
    fun onUsernameChanged(username: String)
     fun onSubmitClicked()
    fun onBackClicked()
}