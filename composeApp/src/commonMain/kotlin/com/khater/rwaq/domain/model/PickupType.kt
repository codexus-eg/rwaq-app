package com.khater.rwaq.domain.model

enum class PickupType {
    BRANCH,
    DRIVE_THRU,
    DELIVERY;

    companion object {
        fun from(value: String?): PickupType =
            entries.firstOrNull { it.name == value.orEmpty().uppercase() } ?: BRANCH
    }
}
