package com.khater.rwaq.presentation.util


import com.khater.rwaq.domain.repository.settings.SettingsRepository
import com.khater.rwaq.domain.util.AppLanguage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.preferredLanguages

actual class AppLocalizer(
    settingsRepository: SettingsRepository
) {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    init {
        coroutineScope.launch {
            settingsRepository.observeAppLanguage().collectLatest { currentLanguage ->
                val iso = currentLanguage.iso.ifEmpty {
                    val deviceIso =
                        NSLocale.preferredLanguages.firstOrNull()?.toString()?.split("-")
                            ?.firstOrNull() ?: AppLanguage.DEFAULT.iso
                    settingsRepository.applyLanguage(AppLanguage.fromIso(deviceIso))
                    deviceIso
                }
                val defaults = NSUserDefaults.standardUserDefaults
                defaults.setObject(arrayListOf(iso), forKey = "AppleLanguages")
            }
        }
    }
}

