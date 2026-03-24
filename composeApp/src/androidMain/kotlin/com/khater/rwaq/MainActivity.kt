package com.khater.rwaq

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.khater.rwaq.presentation.App
import com.khater.rwaq.presentation.util.AppLocalizer
import com.mmk.kmpnotifier.permission.AndroidPermissionUtil
import com.mmk.kmpnotifier.permission.permissionUtil
import io.github.tbib.klocation.AccuracyPriority
import io.github.tbib.klocation.AndroidKLocationService
import io.github.tbib.klocation.KLocationService
import org.koin.android.ext.android.inject
import kotlin.getValue

class MainActivity : ComponentActivity() {

    val permissionUtil: AndroidPermissionUtil by permissionUtil()


    override fun onCreate(savedInstanceState: Bundle?) {
        Color(0xFF98C498)
        Color(0xFFCE9696)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.BLACK
            ),
            navigationBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.BLACK
            )
        )
        super.onCreate(savedInstanceState)
        val localizer: AppLocalizer by inject()
        localizer.applyLocaleToContext()
        AndroidKLocationService.initialization(this, AccuracyPriority.BALANCED_POWER_ACCURACY)
        setContent {
            // 1. Create the service instance
            val locationService = remember { KLocationService() }

            // 2. Setup the Listener (This listens for the user's Yes/No choice)
            locationService.ListenerToPermission()

            // 3. ACTUAL FIX: Trigger the request when the app starts
                 // This checks if permission is missing, and if so, shows the dialog
           locationService.EnableLocation()

            App()
        }
    }
    override fun onStart() {
        super.onStart()
        permissionUtil.askNotificationPermission { isSuccess ->
            if (isSuccess) return@askNotificationPermission
            Toast.makeText(this, "Permissions is required", Toast.LENGTH_SHORT).show()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}