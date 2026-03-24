package com.khater.rwaq.data.source.local.database.car

import androidx.room.TypeConverter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

class CarConverter {

    @TypeConverter
    fun fromColor(color: Color): Int {
        // Converts the Compose Color to an ARGB Integer
        return color.toArgb()
    }

    @TypeConverter
    fun toColor(value: Int): Color {
        // Creates a Compose Color from the ARGB Integer
        return Color(value)
    }
}