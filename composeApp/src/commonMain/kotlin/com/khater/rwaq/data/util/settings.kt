@file:OptIn(ExperimentalSettingsApi::class)

package com.khater.rwaq.data.util

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.flow.Flow

internal val FlowSettings.accessToken: Flow<String>
    get() = getStringFlow(ACCESS_TOKEN, "")

suspend fun FlowSettings.putAccessToken(value: String){
    putString(ACCESS_TOKEN, value)
}

internal val FlowSettings.refreshToken: Flow<String>
    get() = getStringFlow(REFRESH_TOKEN, "")

suspend fun FlowSettings.putRefreshToken(value: String){
    putString(REFRESH_TOKEN, value)
}


internal val FlowSettings.fcmToken: Flow<String>
    get() = getStringFlow(FCM_TOKEN, "")

suspend fun FlowSettings.putFcmToken(value: String) {
    putString(FCM_TOKEN, value)
}


internal val FlowSettings.username: Flow<String>
    get() = getStringFlow(USER_NAME, "")

suspend fun FlowSettings.putUsername(value: String) {
    putString(USER_NAME, value)
}

internal val FlowSettings.phoneNumber: Flow<String>
    get() = getStringFlow(PHONE_NUMBER, "")

suspend fun FlowSettings.putPhoneNumber(value: String) {
    putString(PHONE_NUMBER, value)
}

internal val FlowSettings.email: Flow<String>
    get() = getStringFlow(EMAIL, "")

suspend fun FlowSettings.putEmail(value: String) {
    putString(EMAIL, value)
}

internal val FlowSettings.userPoints: Flow<Int>
    get() = getIntFlow(USER_POINTS, 0)

suspend fun FlowSettings.putUserPoints(value: Int) {
    putInt(USER_POINTS, value)
}

private const val USER_NAME = "user_name"
private const val PHONE_NUMBER = "phone_number"
private const val ACCESS_TOKEN = "access_token"
private const val REFRESH_TOKEN = "refresh_token"
private const val FCM_TOKEN = "fcm_token"
private const val EMAIL = "email"
private const val USER_POINTS = "user_points"
