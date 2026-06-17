package com.khater.rwaq.di

import com.khater.rwaq.data.repository.authentication.AuthenticationRepositoryImpl
import com.khater.rwaq.data.repository.appVersion.AppVersionRepositoryImpl
import com.khater.rwaq.data.repository.branch.BranchRepositoryImpl
import com.khater.rwaq.data.repository.cart.CartRepositoryImpl
import com.khater.rwaq.data.repository.order.OrderRepositoryImpl
import com.khater.rwaq.data.repository.product.ProductRepositoryImpl
import com.khater.rwaq.data.repository.referral.ReferralRepositoryImpl
import com.khater.rwaq.data.repository.settings.SettingsRepositoryImpl
import com.khater.rwaq.data.repository.user.UserRepositoryImpl
 import com.khater.rwaq.domain.repository.authentication.AuthenticationRepository
import com.khater.rwaq.domain.repository.referral.ReferralRepository
import com.khater.rwaq.domain.repository.appVersion.AppVersionRepository
import com.khater.rwaq.domain.repository.branch.BranchRepository
import com.khater.rwaq.domain.repository.cart.CartRepository
import com.khater.rwaq.domain.repository.order.OrderRepository
import com.khater.rwaq.domain.repository.product.ProductRepository
import com.khater.rwaq.domain.repository.settings.SettingsRepository
import com.khater.rwaq.domain.repository.user.UserRepository
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

@OptIn(ExperimentalSettingsApi::class)
val repositoryModule = module {

    singleOf(::Settings)
    single<SettingsRepository>(createdAtStart = true) { SettingsRepositoryImpl(settings = get()) }
    single<AppVersionRepository>{ AppVersionRepositoryImpl(get()) }
    single<AuthenticationRepository>{ AuthenticationRepositoryImpl(get(),get()) }
    single<BranchRepository>{ BranchRepositoryImpl(get(),get()) }
    single<ProductRepository>{ ProductRepositoryImpl(get(), get()) }
    single<CartRepository>{ CartRepositoryImpl(get(), get()) }
    single<OrderRepository>{ OrderRepositoryImpl(get(),get()) }
    single<UserRepository>{ UserRepositoryImpl(get()) }
    single<ReferralRepository>{ ReferralRepositoryImpl(get()) }

}
