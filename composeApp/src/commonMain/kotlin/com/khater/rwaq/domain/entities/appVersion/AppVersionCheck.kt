package com.khater.rwaq.domain.entities.appVersion

data class AppVersionCheck(
    val app: String,
    val platform: String,
    val currentVersion: String,
    val currentBuildNumber: Int,
    val latestVersion: String,
    val latestBuildNumber: Int,
    val minSupportedVersion: String,
    val minSupportedBuildNumber: Int,
    val updateAvailable: Boolean,
    val forceUpdate: Boolean,
    val isUpdateRequired: Boolean,
    val shouldShowUpdateSheet: Boolean,
    val updateUrl: String,
    val title: String,
    val message: String,
    val releaseNotes: String,
    val checkedAt: String
)
