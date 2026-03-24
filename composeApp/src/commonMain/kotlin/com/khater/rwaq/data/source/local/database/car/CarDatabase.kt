@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.khater.rwaq.data.source.local.database.car

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.khater.rwaq.data.source.local.database.order.OrderConverter
import com.khater.rwaq.data.source.local.database.order.OrderDao
import com.khater.rwaq.data.source.local.database.order.OrderLocalDto


@Database(
    entities = [CarLocalDto::class, OrderLocalDto::class],
    version = 1
)
@ConstructedBy(CarDatabaseConstructor::class)
@TypeConverters(CarConverter::class, OrderConverter::class)
abstract class CarDatabase : RoomDatabase() {
    abstract fun carDao(): CarDao
    abstract fun orderDao(): OrderDao // Added Order DAO
}

@Suppress("KotlinNoActualForExpect")
expect object CarDatabaseConstructor: RoomDatabaseConstructor<CarDatabase>{
    override fun initialize(): CarDatabase
}

const val CAR_DATABASE_NAME = "car_database.db"