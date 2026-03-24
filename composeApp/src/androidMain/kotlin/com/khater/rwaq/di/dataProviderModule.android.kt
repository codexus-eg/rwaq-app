package com.khater.rwaq.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.khater.rwaq.data.source.local.database.car.CAR_DATABASE_NAME
import com.khater.rwaq.data.source.local.database.car.CarDatabase
import org.koin.core.scope.Scope


actual fun Scope.getDatabaseBuilder(): RoomDatabase.Builder<CarDatabase> {
    val context = get<Context>()
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath(CAR_DATABASE_NAME)

    return Room.databaseBuilder<CarDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}