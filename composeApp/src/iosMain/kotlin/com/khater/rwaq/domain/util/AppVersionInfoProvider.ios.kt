package com.khater.rwaq.domain.util

import platform.Foundation.NSBundle

actual object AppVersionInfoProvider {
    actual val platform: String = "ios"
    actual val versionName: String
        get() = bundleValue("CFBundleShortVersionString").ifBlank { "1.0.0" }
    actual val buildNumber: Int
        get() = bundleValue("CFBundleVersion").toIntOrNull() ?: 0

    private fun bundleValue(key: String): String =
        NSBundle.mainBundle.objectForInfoDictionaryKey(key)?.toString().orEmpty()
}
