package com.khater.rwaq.data.dto.branch

import com.khater.rwaq.domain.entities.branch.Branch
import com.khater.rwaq.domain.entities.branch.BranchStatus
import com.khater.rwaq.domain.entities.branch.BranchWorkTime
import com.khater.rwaq.domain.entities.branch.Location
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BranchDto(
    @SerialName("_id")
    val id: String? = null,
    @SerialName("branchName")
    val branchName: String? = null,
    @SerialName("isBranchAvailableForDriveThru")
    val isBranchAvailableForDriveThru: Boolean? = null,
    @SerialName("branchStatus")
    val branchStatus: String? = null,
    @SerialName("distanceAwayFromYou")
    val distanceAwayFromYou: Double? = null,
    @SerialName("branchLocation")
    val branchLocation: LocationDto? = null,
    @SerialName("workTime")
    val workTime: List<BranchWorkTimeDto>? = null,
) {
    fun toDomain() = Branch(
        id  = id ?: "",
        branchName = branchName ?: "",
        isBranchAvailableForDriveThru = isBranchAvailableForDriveThru == true,
        branchStatus = when(branchStatus){
            "OPEN" -> BranchStatus.OPEN
            "CLOSED" -> BranchStatus.CLOSED
            "TEMPORARILY_CLOSED" -> BranchStatus.TEMPORARILY_CLOSED
            "COMING_SOON" -> BranchStatus.COMING_SOON
            else -> BranchStatus.CLOSED
        },
        distanceAwayFromYou = distanceAwayFromYou ?: 0.0,
        branchLocation = branchLocation?.toDomain() ?: Location(latitude = 0.0, longitude = 0.0),
        workTime = workTime?.map { it.toDomain() } ?: emptyList()
    )
}


@Serializable
data class BranchWorkTimeDto(
    val day: String? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val isAvailableAllDay: Boolean? = null,
) {
    fun toDomain() = BranchWorkTime(
        day = day ?: "",
        startTime = startTime ?: "",
        endTime = endTime ?: "",
        isAvailableAllDay = isAvailableAllDay == true
    )
}

@Serializable
data class LocationDto(
    val latitude: Double? = null,
    val longitude: Double? = null,
){
    fun toDomain() = Location(
        latitude = latitude ?: 0.0,
        longitude = longitude ?: 0.0
    )
}


