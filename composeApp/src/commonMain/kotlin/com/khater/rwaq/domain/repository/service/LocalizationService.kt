package com.khater.rwaq.domain.repository.service

import com.khater.rwaq.domain.repository.settings.SettingsRepository
import com.khater.rwaq.domain.util.AppLanguage
import kotlinx.coroutines.flow.StateFlow


class LocalizationService(
    private val settingsRepository: SettingsRepository
) {
    fun observeLanguage(): StateFlow<AppLanguage> =
        settingsRepository.observeAppLanguage()

    fun getCurrentLanguage(): AppLanguage =
        settingsRepository.getCurrentAppLanguage()
}