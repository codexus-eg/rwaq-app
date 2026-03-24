package com.khater.rwaq

import co.touchlab.kermit.Logger
import com.khater.rwaq.domain.useCases.auth.UpdateFcmTokenUseCase
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.PayloadData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object AppInitializer : KoinComponent{

    private val updateFcmTokenUseCase: UpdateFcmTokenUseCase by inject()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun onApplicationStart() {
        onApplicationStartPlatformSpecific()
        scope.launch {

        NotifierManager.getPushNotifier().subscribeToTopic("all")
        }

        NotifierManager.addListener(object : NotifierManager.Listener {
            override fun onNewToken(token: String) {
                scope.launch {
                    try {
                        updateFcmTokenUseCase(token)
                        Logger.i("Backend updated with new FCM token")
                    } catch (e: Exception) {
                        Logger.e("Failed to update FCM token: ${e.message}")
                    }
                }
                println("Push Notification onNewToken: $token")
                logMessage("Push Notification onNewToken: $token")
                Logger.i("Push Notification onNewToken: $token")
            }

            override fun onNotificationClicked(data: PayloadData) {
                super.onNotificationClicked(data)
                println("Notification clicked, Notification payloadData: $data")
                logMessage("Notification clicked, Notification payloadData: $data")
                Logger.i("Notification clicked, Notification payloadData: $data")

            }
            override fun onPayloadData(data: PayloadData) {
                super.onPayloadData(data)
                println("Push Notification payloadData: $data")
                logMessage("Push Notification payloadData: $data")
                Logger.i("Push Notification payloadData: $data")

            }
        })
    }
}