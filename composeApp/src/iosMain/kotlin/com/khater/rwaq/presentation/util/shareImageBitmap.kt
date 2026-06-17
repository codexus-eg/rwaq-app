
package com.khater.rwaq.presentation.util

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import network.chaintech.cmpimagepickncrop.utils.toUIImage
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIImage

@OptIn(ExperimentalForeignApi::class)
actual suspend fun shareImageBitmap(bitmap: ImageBitmap, text: String) {
    // Convert Compose ImageBitmap → UIImage
    val uiImage: UIImage = bitmap.toUIImage()
        ?: error("Failed to convert ImageBitmap to UIImage")

    // Items to share: both the image and the plain-text caption
    val activityItems: List<Any> = listOf(uiImage, text)

    val viewController = UIActivityViewController(
        activityItems = activityItems,
        applicationActivities = null,
    )

    // Present from the key window's root view controller
    UIApplication.sharedApplication.keyWindow
        ?.rootViewController
        ?.presentViewController(
            viewControllerToPresent = viewController,
            animated = true,
            completion = null,
        )
}

actual val storesAppLink:String = "https://apps.apple.com/eg/app/%D8%B1%D9%88%D8%A7%D9%82/id6761053148?l=ar"