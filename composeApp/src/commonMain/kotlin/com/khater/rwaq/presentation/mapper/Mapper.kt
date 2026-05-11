package com.khater.rwaq.presentation.mapper
import com.khater.rwaq.domain.entities.branch.Branch
import com.khater.rwaq.domain.entities.branch.BranchWorkTime
import com.khater.rwaq.domain.entities.branch.Location
import com.khater.rwaq.presentation.screens.branchScreen.uiState.BranchUiState
import com.khater.rwaq.presentation.screens.branchScreen.uiState.BranchWorkTimeUiState
import com.khater.rwaq.presentation.screens.branchScreen.uiState.LocationUiState


fun Branch.toUi() = BranchUiState(
    id = id,
    branchName = branchName,
    isBranchAvailableForDriveThru = isBranchAvailableForDriveThru,
    branchStatus = branchStatus,
    distanceAwayFromYou = distanceAwayFromYou,
    branchLocation = branchLocation.toUi(),
    workTime = workTime.map { it.toUi() }
)

fun Location.toUi() = LocationUiState(
    latitude = latitude,
    longitude = longitude
)
fun BranchWorkTime.toUi() = BranchWorkTimeUiState(
    day = day,
    startTime = startTime,
    endTime = endTime,
    isAvailableAllDay = isAvailableAllDay
)

//
//fun  LocationUiState .toMarker(branchName: String) = com.swmansion.kmpmaps.core.Marker(
//    coordinates = com.swmansion.kmpmaps.core.Coordinates(latitude = latitude, longitude = longitude),
//    title = branchName
//)

