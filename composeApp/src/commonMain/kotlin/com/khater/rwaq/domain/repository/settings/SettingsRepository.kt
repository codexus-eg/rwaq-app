package com.khater.rwaq.domain.repository.settings

import com.khater.rwaq.domain.util.AppLanguage
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    suspend fun applyLanguage(appLanguage: AppLanguage)
    fun observeAppLanguage(): StateFlow<AppLanguage>
    fun getCurrentAppLanguage(): AppLanguage
}