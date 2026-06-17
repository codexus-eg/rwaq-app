package com.khater.rwaq.presentation.util

import androidx.compose.ui.graphics.ImageBitmap

expect suspend fun shareImageBitmap(bitmap: ImageBitmap, text: String)

expect val storesAppLink: String