package com.khater.rwaq.presentation.util

import platform.Foundation.NSURL
import platform.UIKit.*
import platform.StoreKit.SKStoreReviewController


// handle app ID when publish into app store
private const val APP_STORE_ID = "6761053148"

actual class AppStoreManager {

    actual fun rateApp() {
        SKStoreReviewController.requestReview()

//        val url = NSURL(
//            string = "https://apps.apple.com/app/id$APP_STORE_ID?action=write-review"
//        )
//
//        UIApplication.sharedApplication.openURL(
//            url,
//            options = emptyMap<Any?, Any?>(),
//            completionHandler = null
//        )
    }


    actual fun shareApp(userId:String) {
        val shareText = "The best coffee app ever! Smarter shop. Better mode:"
        val linkString = "https://apps.apple.com/app/id${APP_STORE_ID}"

        // Create a proper NSURL for the link
        val url = NSURL.URLWithString(linkString)

        // Create the items list.
        // If the URL failed to parse, we fall back to just the link string.
        val items = mutableListOf<Any>().apply {
            add(shareText)
            if (url != null) add(url) else add(linkString)
        }

        val activityVC = UIActivityViewController(
            activityItems = items,
            applicationActivities = null
        )

        val rootVC = UIApplication.sharedApplication
            .keyWindow
            ?.rootViewController

        // On iPad, UIActivityViewController must be presented as a popover
        // to prevent a crash.
        activityVC.popoverPresentationController?.sourceView = rootVC?.view

        rootVC?.presentViewController(
            activityVC,
            animated = true,
            completion = null
        )
    }
}