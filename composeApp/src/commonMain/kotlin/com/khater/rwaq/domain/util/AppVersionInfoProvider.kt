package com.khater.rwaq.domain.util

expect object AppVersionInfoProvider {
    val platform: String
    val versionName: String
    val buildNumber: Int
}
