package com.khater.rwaq.data.dto.appVersion

import com.khater.rwaq.domain.entities.appVersion.AppVersionCheck
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppVersionCheckResponseDto(
    @SerialName("success")
    val success: Boolean = false,
    @SerialName("data")
    val data: AppVersionCheckDto? = null
)

@Serializable
data class AppVersionCheckDto(
    @SerialName("app")
    val app: String = "",
    @SerialName("platform")
    val platform: String = "",
    @SerialName("currentVersion")
    val currentVersion: String = "",
    @SerialName("currentBuildNumber")
    val currentBuildNumber: Int = 0,
    @SerialName("latestVersion")
    val latestVersion: String = "",
    @SerialName("latestBuildNumber")
    val latestBuildNumber: Int = 0,
    @SerialName("minSupportedVersion")
    val minSupportedVersion: String = "",
    @SerialName("minSupportedBuildNumber")
    val minSupportedBuildNumber: Int = 0,
    @SerialName("updateAvailable")
    val updateAvailable: Boolean = false,
    @SerialName("forceUpdate")
    val forceUpdate: Boolean = false,
    @SerialName("isUpdateRequired")
    val isUpdateRequired: Boolean = false,
    @SerialName("shouldShowUpdateSheet")
    val shouldShowUpdateSheet: Boolean = false,
    @SerialName("updateUrl")
    val updateUrl: String = "",
    @SerialName("title")
    val title: String = "",
    @SerialName("message")
    val message: String = "",
    @SerialName("releaseNotes")
    val releaseNotes: String = "",
    @SerialName("checkedAt")
    val checkedAt: String = ""
)

fun AppVersionCheckDto.toDomain(): AppVersionCheck =
    AppVersionCheck(
        app = app,
        platform = platform,
        currentVersion = currentVersion,
        currentBuildNumber = currentBuildNumber,
        latestVersion = latestVersion,
        latestBuildNumber = latestBuildNumber,
        minSupportedVersion = minSupportedVersion,
        minSupportedBuildNumber = minSupportedBuildNumber,
        updateAvailable = updateAvailable,
        forceUpdate = forceUpdate,
        isUpdateRequired = isUpdateRequired,
        shouldShowUpdateSheet = shouldShowUpdateSheet,
        updateUrl = updateUrl,
        title = title,
        message = message,
        releaseNotes = releaseNotes,
        checkedAt = checkedAt
    )
