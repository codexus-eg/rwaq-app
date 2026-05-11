package com.khater.rwaq.di


import com.khater.rwaq.domain.repository.service.LocalizationService
import com.khater.rwaq.presentation.util.AppLocalizer
import com.khater.rwaq.presentation.util.AppStoreManager
import com.khater.rwaq.presentation.util.ReferralHelper
import org.koin.android.ext.koin.androidContext
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
    factory { ReferralHelper(context = androidContext()) }
}
