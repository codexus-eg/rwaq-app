package com.khater.rwaq.data.source.remote.appVersion

import com.khater.rwaq.data.dto.appVersion.AppVersionCheckResponseDto
import com.khater.rwaq.data.util.getJson
import com.khater.rwaq.data.util.safeWrapper
import com.khater.rwaq.domain.repository.service.LocalizationService
import io.ktor.client.HttpClient

class AppVersionRemoteDataSource(
    private val httpClient: HttpClient,
    private val localizationService: LocalizationService
) : AppVersionDataSource {

    override suspend fun checkAppVersion(
        platform: String,
        version: String,
        buildNumber: Int,
        app: String
    ): AppVersionCheckResponseDto =
        safeWrapper {
            httpClient.getJson<AppVersionCheckResponseDto>(
                path = APP_VERSION_CHECK_ENDPOINT,
                headerParams = mapOf(
                    ACCEPT_LANGUAGE to localizationService.getCurrentLanguage().iso
                ),
                queryParams = mapOf(
                    PLATFORM_PARAMETER to platform,
                    VERSION_PARAMETER to version,
                    BUILD_NUMBER_PARAMETER to buildNumber.toString(),
                    APP_PARAMETER to app
                )
            )
        }

    companion object {
        private const val APP_VERSION_CHECK_ENDPOINT = "api/app-version/check"
        private const val ACCEPT_LANGUAGE = "Accept-Language"
        private const val PLATFORM_PARAMETER = "platform"
        private const val VERSION_PARAMETER = "version"
        private const val BUILD_NUMBER_PARAMETER = "buildNumber"
        private const val APP_PARAMETER = "app"
    }
}
