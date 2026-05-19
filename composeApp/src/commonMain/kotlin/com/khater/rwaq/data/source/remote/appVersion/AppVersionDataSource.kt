package com.khater.rwaq.data.source.remote.appVersion

import com.khater.rwaq.data.dto.appVersion.AppVersionCheckResponseDto

interface AppVersionDataSource {
    suspend fun checkAppVersion(
        platform: String,
        version: String,
        buildNumber: Int,
        app: String = "CUSTOMER"
    ): AppVersionCheckResponseDto
}
