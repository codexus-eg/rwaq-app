package com.khater.rwaq.di


import com.khater.rwaq.domain.repository.service.LocalizationService
import com.khater.rwaq.presentation.util.AppLocalizer
import com.khater.rwaq.presentation.util.AppStoreManager
import io.github.tbib.klocation.KLocationService
import org.koin.dsl.module

actual val platformModule = module {
    single<LocalizationService> { LocalizationService(settingsRepository = get()) }
    single<AppLocalizer>(
        createdAtStart = true
    ) {
        AppLocalizer(
            context = get(),
            settingsRepository = get()
        )
    }

    single<AppStoreManager> {
        AppStoreManager(
            context = get()
        )
    }
    //single { KLocationService() }

}