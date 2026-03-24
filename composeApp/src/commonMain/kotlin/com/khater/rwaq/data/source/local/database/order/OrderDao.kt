package com.khater.rwaq.data.source.local.database.order

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderLocalDto)

    @Update
    suspend fun updateOrder(order: OrderLocalDto)

    @Query("SELECT * FROM local_orders")
    fun getAllOrders(): Flow<List<OrderLocalDto>>

    @Query("DELETE FROM local_orders WHERE id = :id")
    suspend fun deleteOrderById(id: String)
    
    @Query("DELETE FROM local_orders")
    suspend fun clearCart()
}