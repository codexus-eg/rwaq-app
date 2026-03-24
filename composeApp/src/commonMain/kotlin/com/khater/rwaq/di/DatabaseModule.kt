package com.khater.rwaq.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.khater.rwaq.data.source.local.database.car.CarDao
import com.khater.rwaq.data.source.local.database.car.CarDatabase
import com.khater.rwaq.data.source.local.database.order.OrderDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module

internal val localDataModule = module {

    single(named(CarDatabaseBuilder)) {
        getDatabaseBuilder()
    }

    single<CarDatabase> {
        getAppDatabase(get(named(CarDatabaseBuilder)))
    }
    single<CarDao> { get<CarDatabase>().carDao() }
    single<OrderDao> { get<CarDatabase>().orderDao() }

}

expect fun Scope.getDatabaseBuilder(): RoomDatabase.Builder<CarDatabase>

private fun getAppDatabase(
    builder: RoomDatabase.Builder<CarDatabase>
): CarDatabase =
    builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()

private const val CarDatabaseBuilder = "CarDatabaseBuilder"