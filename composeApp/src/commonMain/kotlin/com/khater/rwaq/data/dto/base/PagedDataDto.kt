package com.khater.rwaq.data.dto.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PagedDataDto<T>(
    @SerialName("data")
    val data: List<T>,
    @SerialName("page")
    val pageNumber: Int,
    @SerialName("count")
    val pageSize: Int,
    @SerialName("total")
    val totalItems: Int,
    @SerialName("pages")
    val totalPages: Int,
    @SerialName("userPoints")
    val userPoints: Int? = null
)
