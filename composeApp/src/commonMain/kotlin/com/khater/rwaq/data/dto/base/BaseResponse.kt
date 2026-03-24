package com.khater.rwaq.data.dto.base

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val success: Boolean,
    val count: Int? = null,
    val total: Int? = null,
    val page: Int? = null,
    val pages: Int? = null,
    val data: T
)