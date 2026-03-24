package com.khater.rwaq

import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import platform.Foundation.NSLog

actual fun onApplicationStartPlatformSpecific() {
    NotifierManager.initialize(NotificationPlatformConfiguration.Ios(
        showPushNotification= true,
        askNotificationPermissionOnStart=true
    ))
}

actual fun logMessage(message: String) {
    NSLog(message)
}