package com.khater.rwaq.presentation.screens.profileScreen.uiState

import com.khater.rwaq.domain.util.AppLanguage

interface ProfileInteractionListener {
    fun onLogout()
    fun onDeleteAccount() // Added
    fun toggleDialog(type: ProfileDialogType)
    fun onBack()
    fun onOptionSelected(option: ProfileOption)
    fun onLanguageClicked()
    fun onDismissLanguageDialog()
    fun onConfirmLanguageSelection()
    fun onSelectLanguage(appLanguage: AppLanguage)
    fun onClickLogin()
    fun onDismissGuestDialog()
}