package com.khater.rwaq.di

import coil3.ImageLoader
import com.khater.rwaq.domain.repository.service.LocalizationService
import com.khater.rwaq.presentation.util.AppLocalizer
import com.khater.rwaq.presentation.util.AppStoreManager
import com.khater.rwaq.presentation.util.ReferralHelper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformModule = module {
    singleOf(::ImageLoader)
     single<LocalizationService> { LocalizationService(settingsRepository = get()) }
    single<AppLocalizer> (
        createdAtStart = true
    ){ AppLocalizer(
        settingsRepository = get()
    ) }

    single<AppStoreManager>(
        createdAtStart = true
    ){
        AppStoreManager()
    }
    factory { ReferralHelper() }
}
