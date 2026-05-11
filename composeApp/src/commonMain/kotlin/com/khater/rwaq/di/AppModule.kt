package com.khater.rwaq.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            repositoryModule, platformModule, viewModelModule, dispatcherModule, networkModule
        ,dataSourceModule,useCaseModule,localDataModule,referralModule)
    }
}