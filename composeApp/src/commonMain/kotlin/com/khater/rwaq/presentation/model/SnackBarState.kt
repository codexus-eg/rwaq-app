package com.khater.rwaq.presentation.model

data class SnackBarState(
    val isVisible: Boolean = false,
    val title: String? = null,
    val message: String? = null,
    val isSuccess: Boolean = true
)