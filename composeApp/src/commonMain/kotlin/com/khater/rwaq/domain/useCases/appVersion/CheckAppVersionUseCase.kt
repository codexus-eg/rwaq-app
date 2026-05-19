package com.khater.rwaq.domain.useCases.appVersion

import com.khater.rwaq.domain.entities.appVersion.AppVersionCheck
import com.khater.rwaq.domain.repository.appVersion.AppVersionRepository
import com.khater.rwaq.domain.util.AppVersionInfoProvider

class CheckAppVersionUseCase(
    private val appVersionRepository: AppVersionRepository
) {
    suspend operator fun invoke(app: String = "CUSTOMER"): AppVersionCheck? =
        appVersionRepository.checkAppVersion(
            platform = AppVersionInfoProvider.platform,
            version = AppVersionInfoProvider.versionName,
            buildNumber = AppVersionInfoProvider.buildNumber,
            app = app
        )
}
