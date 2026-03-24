package com.khater.rwaq.presentation.util

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

actual class AppStoreManager(
    private val context: Context,
) {

    actual  fun rateApp() {
        val packageName = context.packageName

        val intent = Intent(
            Intent.ACTION_VIEW,
            "market://details?id=$packageName".toUri()
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            context.startActivity(intent)
        } catch (_: Exception) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/details?id=$packageName".toUri()
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }

    actual fun shareApp(userId:String) {
        try {
            val packageName = context.packageName

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    "The best coffee app ever! Smarter shop. Better mode:\nhttps://rwaq-b04b189d2e97.herokuapp.com/redirect?userId=refer$userId"
                )
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            val chooser = Intent.createChooser(intent, "Share via").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(chooser)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}