package com.khater.rwaq.domain.model

data class PagedData<T>(
    val data: List<T>,
    val totalItems: Int,
    val isLastPage: Boolean,
)