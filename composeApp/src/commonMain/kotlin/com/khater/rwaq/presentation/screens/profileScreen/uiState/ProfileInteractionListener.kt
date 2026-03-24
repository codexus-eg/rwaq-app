package com.khater.rwaq.presentation.screens.profileScreen.uiState

import com.khater.rwaq.domain.util.AppLanguage

interface ProfileInteractionListener {
    fun onLogout()
    fun toggleDialog()
    fun onBack()
    fun onOptionSelected(option: ProfileOption)
    fun onLanguageClicked()
    fun onDismissLanguageDialog()
    fun onConfirmLanguageSelection()
    fun onSelectLanguage(appLanguage: AppLanguage)


}