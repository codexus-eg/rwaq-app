package com.khater.rwaq.data.source.local.database.car

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "local_cars")
data class CarLocalDto(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "car_name")
    val carName: String,
    @ColumnInfo(name = "car_number")
    val carNumber: String,
    @ColumnInfo(name = "car_image_url")
    val carImage: String,
    @ColumnInfo(name = "car_color")
    val carColor: Color,
    @ColumnInfo(name = "car_color_name")
    val carColorName: String
)
