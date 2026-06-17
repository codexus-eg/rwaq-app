// androidMain/kotlin/com/yourapp/share/ShareAppImage.android.kt

package com.khater.rwaq.presentation.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.content.FileProvider
import okhttp3.internal.platform.PlatformRegistry.applicationContext
import java.io.File
import java.io.FileOutputStream

private const val FILE_PROVIDER_AUTHORITY = "com.khater.rwaq.provider"

actual suspend fun shareImageBitmap(bitmap: ImageBitmap, text: String,) {
    val context = AndroidContext.get()
     val uri: Uri = saveImageToCache(context, bitmap)

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/png"
        putExtra(Intent.EXTRA_TEXT, text)
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    val chooser = Intent.createChooser(intent, "Share via")
    chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(chooser)
}


private fun saveImageToCache(context: Context, bitmap: ImageBitmap): Uri {
    val dir = File(context.cacheDir, "shared_images").apply { mkdirs() }
    val file = File(dir, "share_qr_${System.currentTimeMillis()}.png")

    FileOutputStream(file).use { out ->
        bitmap.asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, out)
    }

    return FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, file)
}



actual val storesAppLink:String = "https://play.google.com/store/apps/details?id=com.khater.rwaq"