package com.khater.rwaq.data.source.local.database.car

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCar(car: CarLocalDto)

    @Query("SELECT * FROM local_cars")
     fun getAllCars(): Flow<List<CarLocalDto>>

    @Query("DELETE FROM local_cars WHERE id = :id")
    suspend fun deleteCarById(id: String)

}