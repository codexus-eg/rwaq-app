package com.khater.rwaq.data.repository.settings


import com.khater.rwaq.data.source.local.setting.appLanguage
import com.khater.rwaq.domain.repository.settings.SettingsRepository
import com.khater.rwaq.domain.util.AppLanguage
import com.russhwolf.settings.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SettingsRepositoryImpl(
    private val settings: Settings,
) : SettingsRepository {
    private val observableLanguage: MutableStateFlow<String> =
        MutableStateFlow(settings.appLanguage)

    override suspend fun applyLanguage(appLanguage: AppLanguage) {
        println("applyLanguage: $appLanguage")
        settings.appLanguage = appLanguage.iso.also { observableLanguage.emit(appLanguage.iso) }
    }

    override fun observeAppLanguage(): StateFlow<AppLanguage> {
        println("observeAppLanguage: ${observableLanguage.value.toAppLanguage()}")
      return  observableLanguage.map { it.toAppLanguage() }
            .stateIn(
                scope = CoroutineScope(Dispatchers.IO),
                started = SharingStarted.Companion.Eagerly,
                initialValue = observableLanguage.value.toAppLanguage()
            )
    }

    override fun getCurrentAppLanguage(): AppLanguage = settings.appLanguage.toAppLanguage()
    private fun String.toAppLanguage(): AppLanguage {
        return when (this) {
            AppLanguage.ENGLISH.iso -> AppLanguage.ENGLISH
            AppLanguage.ARABIC.iso -> AppLanguage.ARABIC
            else -> AppLanguage.DEFAULT
        }
    }
}