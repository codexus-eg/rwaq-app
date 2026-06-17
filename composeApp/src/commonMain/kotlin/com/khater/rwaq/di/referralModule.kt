package com.khater.rwaq.di

import com.khater.rwaq.presentation.util.ReferralManager
import org.koin.dsl.module

val referralModule = module {
    single { ReferralManager(helper = get(), referralRepository = get(), settings = get()) }
}