package com.khater.rwaq

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.khater.rwaq.data.source.local.setting.pendingReferralCode
import com.khater.rwaq.domain.location.NativeLocationActivityProvider
import com.khater.rwaq.presentation.App
import com.khater.rwaq.presentation.util.AppLocalizer
import com.khater.rwaq.presentation.util.parseReferralCode
import com.mmk.kmpnotifier.permission.AndroidPermissionUtil
import com.mmk.kmpnotifier.permission.permissionUtil
import com.russhwolf.settings.Settings
import org.koin.android.ext.android.inject
import kotlin.getValue

class MainActivity : ComponentActivity() {

    val permissionUtil: AndroidPermissionUtil by permissionUtil()

    // Same Settings singleton the ReferralManager reads, so a captured code is
    // visible to the claim step after login.
    private val settings: Settings by inject()


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
        NativeLocationActivityProvider.attach(this)
        // Capture a referral code if the app was opened via an invite App Link
        // (installed-app case). The not-installed case is handled separately by
        // the Play Store install referrer in ReferralHelper.
        handleReferralIntent(intent)
        val localizer: AppLocalizer by inject()
        localizer.applyLocaleToContext()
        setContent {
            App()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleReferralIntent(intent)
    }

    // Parse the referral code from an incoming invite link and store it as the
    // pending code (without overwriting one already waiting to be claimed). The
    // actual reward happens after login via ReferralManager.claimPendingReferral.
    private fun handleReferralIntent(intent: Intent?) {
        val data = intent?.data?.toString() ?: return
        val code = parseReferralCode(data)
        if (!code.isNullOrBlank() && settings.pendingReferralCode.isBlank()) {
            settings.pendingReferralCode = code
        }
    }

    override fun onStart() {
        super.onStart()
        permissionUtil.askNotificationPermission { isSuccess ->
            if (isSuccess) return@askNotificationPermission
            Toast.makeText(this, "Permissions is required", Toast.LENGTH_SHORT).show()
        }
    }

    @Suppress("DEPRECATION", "OVERRIDE_DEPRECATION")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        if (NativeLocationActivityProvider.onRequestPermissionsResult(requestCode, grantResults)) {
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDestroy() {
        NativeLocationActivityProvider.detach(this)
        super.onDestroy()
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
