package com.khater.rwaq.domain.util

import com.khater.rwaq.BuildConfig

actual object AppVersionInfoProvider {
    actual val platform: String = "android"
    actual val versionName: String = BuildConfig.VERSION_NAME
    actual val buildNumber: Int = BuildConfig.VERSION_CODE
}
