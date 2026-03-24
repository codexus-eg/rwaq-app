package com.khater.rwaq.data.source.local.database.order

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import com.khater.rwaq.domain.entities.order.OrderExtension

@Entity(tableName = "local_orders")
data class OrderLocalDto(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "order_name")
    val name: String,
    @ColumnInfo(name = "product_id")
    val productId: String,
    @ColumnInfo(name = "order_imageUrl")
    val imageUrl: String,
    @ColumnInfo(name = "total_price")
    val totalPrice: Double,
    @ColumnInfo(name = "item_price")
    val itemPrice: Double,
    @ColumnInfo(name = "count")
    val count: Int,
    @ColumnInfo(name = "size")
    val size: String,
    @ColumnInfo(name = "extensions")
    val extensions: List<OrderExtension>,
    @ColumnInfo(name = "isReward")
    val isReward: Boolean,

    // --- NEW COLUMNS ---
    @ColumnInfo(name = "branch_id") val branchId: String,
    @ColumnInfo(name = "branch_name") val branchName: String,
    @ColumnInfo(name = "is_pickup") val isPickupFromBranch: Boolean,
    @ColumnInfo(name = "car_name") val carName: String?,
    @ColumnInfo(name = "car_number") val carNumber: String?,
    @ColumnInfo(name = "car_color") val carColor: Color?

)