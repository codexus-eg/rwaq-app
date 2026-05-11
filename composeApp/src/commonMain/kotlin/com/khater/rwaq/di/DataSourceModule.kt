package com.khater.rwaq.di

import com.khater.rwaq.data.source.remote.branch.BranchDataSource
import com.khater.rwaq.data.source.remote.branch.BranchRemoteDataSource
import com.khater.rwaq.data.source.remote.cart.CartDataSource
import com.khater.rwaq.data.source.remote.cart.CartRemoteDataSource
import com.khater.rwaq.data.source.remote.order.OrderDataSource
import com.khater.rwaq.data.source.remote.order.OrderRemoteDataSource
import com.khater.rwaq.data.source.remote.product.ProductDataSource
import com.khater.rwaq.data.source.remote.product.ProductRemoteDataSource
import com.khater.rwaq.data.source.remote.user.UserRemoteDataSource
import com.khater.rwaq.data.source.remote.user.UserRemoteDataSourceImpl
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import org.koin.dsl.module

@OptIn(ExperimentalSettingsApi::class)
val dataSourceModule = module {
    single<FlowSettings> {
        (Settings() as ObservableSettings).toFlowSettings()
    }
    single<BranchDataSource>{ BranchRemoteDataSource(get()) }
    single<CartDataSource>{ CartRemoteDataSource(get(),get()) }
    single<OrderDataSource>{ OrderRemoteDataSource(get(),get()) }
    single<ProductDataSource>{ ProductRemoteDataSource(get(),get()) }
    single<UserRemoteDataSource>{ UserRemoteDataSourceImpl(get(),get ()) }

}