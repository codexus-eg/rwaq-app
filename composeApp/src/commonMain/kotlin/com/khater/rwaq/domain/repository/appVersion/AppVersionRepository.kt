package com.khater.rwaq.domain.repository.appVersion

import com.khater.rwaq.domain.entities.appVersion.AppVersionCheck

interface AppVersionRepository {
    suspend fun checkAppVersion(
        platform: String,
        version: String,
        buildNumber: Int,
        app: String = "CUSTOMER"
    ): AppVersionCheck?
}
