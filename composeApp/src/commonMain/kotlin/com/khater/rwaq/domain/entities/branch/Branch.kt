package com.khater.rwaq.domain.entities.branch

import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.StringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.close
import rwaq.composeapp.generated.resources.coming_soon
import rwaq.composeapp.generated.resources.open
import rwaq.composeapp.generated.resources.temporarily_closed

data class Branch(
    val id: String,
    val branchName: String,
    val isBranchAvailableForDriveThru: Boolean,
    val branchStatus: BranchStatus,
    val distanceAwayFromYou: Double,
    val branchLocation: Location,
    val workTime: List<BranchWorkTime>
)
enum class BranchStatus(val status: StringResource, val color: Color) {
    OPEN(status = Res.string.open, color = Color(0xFF61E061)),
    CLOSED(status = Res.string.close, color = Color(0xFFF6543C)),
    TEMPORARILY_CLOSED(status = Res.string.temporarily_closed, color = Color(0xFFF6DD3C)),
    COMING_SOON(status = Res.string.coming_soon, color = Color(0xFF3CF6F6)),
}
data class BranchWorkTime(
    val day: String,
    val startTime: String,
    val endTime: String,
    val isAvailableAllDay: Boolean,
)

data class Location(
    val latitude: Double,
    val longitude: Double,
)


