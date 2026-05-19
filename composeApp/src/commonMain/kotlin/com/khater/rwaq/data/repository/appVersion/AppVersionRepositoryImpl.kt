package com.khater.rwaq.data.repository.appVersion

import com.khater.rwaq.data.dto.appVersion.toDomain
import com.khater.rwaq.data.source.remote.appVersion.AppVersionDataSource
import com.khater.rwaq.domain.entities.appVersion.AppVersionCheck
import com.khater.rwaq.domain.repository.appVersion.AppVersionRepository

class AppVersionRepositoryImpl(
    private val appVersionDataSource: AppVersionDataSource
) : AppVersionRepository {

    override suspend fun checkAppVersion(
        platform: String,
        version: String,
        buildNumber: Int,
        app: String
    ): AppVersionCheck? {
        val response = appVersionDataSource.checkAppVersion(
            platform = platform,
            version = version,
            buildNumber = buildNumber,
            app = app
        )
        return response.data?.takeIf { response.success }?.toDomain()
    }
}
