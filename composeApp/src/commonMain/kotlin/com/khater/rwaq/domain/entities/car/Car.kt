package com.khater.rwaq.domain.entities.car

import androidx.compose.ui.graphics.Color

data class Car(
    val id: String,
    val name: String,
    val imageUrl: String,
    val color: Color,
    val colorName: String,
    val carNumber:String,
)
