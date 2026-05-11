package com.khater.rwaq.di

import com.khater.rwaq.presentation.util.ReferralManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val dispatcherModule = module {
    single<CoroutineDispatcher> { Dispatchers.IO }
    single { CoroutineScope(SupervisorJob() + Dispatchers.Default) }

}
