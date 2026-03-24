package com.khater.rwaq.data.source.local.database.order

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.TypeConverter
import com.khater.rwaq.domain.entities.order.OrderExtension
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class OrderConverter {
    @TypeConverter
    fun fromExtensionsList(value: List<OrderExtension>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toExtensionsList(value: String): List<OrderExtension> {
        return try {
            Json.decodeFromString(value)
        } catch (e: Exception) {
            emptyList()
        }
    }
}