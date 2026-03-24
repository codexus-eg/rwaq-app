package com.khater.rwaq.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.khater.rwaq.data.source.local.database.car.CAR_DATABASE_NAME
import com.khater.rwaq.data.source.local.database.car.CarDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.scope.Scope
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual fun Scope.getDatabaseBuilder(): RoomDatabase.Builder<CarDatabase> {
    val dbPath = documentDirectory() + "/$CAR_DATABASE_NAME"
    return Room.databaseBuilder<CarDatabase>(name = dbPath)
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )
    return requireNotNull(documentDirectory?.path)
}