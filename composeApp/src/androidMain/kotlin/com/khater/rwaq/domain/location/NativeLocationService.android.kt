package com.khater.rwaq.domain.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.isActive
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

actual class NativeLocationService actual constructor() {

    actual suspend fun getCurrentLocation(): NativeLocationResult {
        val context = NativeLocationActivityProvider.context
            ?: return NativeLocationResult.Unavailable("Android context is not attached.")

        if (!isLocationEnabled()) return NativeLocationResult.ServicesDisabled

        val activity = NativeLocationActivityProvider.currentActivity
            ?: return NativeLocationResult.Unavailable("Android activity is not attached.")

        if (!hasLocationPermission()) {
            val granted = NativeLocationPermissionDelegate.request(activity)
            if (!granted) {
                return NativeLocationResult.PermissionDenied(
                    forever = isPermissionDeniedForever(activity)
                )
            }
        }

        val client = LocationServices.getFusedLocationProviderClient(context)
        return try {
            val location = withTimeoutOrNull(LOCATION_CURRENT_TIMEOUT_MILLIS) {
                requestCurrentLocation(client)
            }
                ?: requestLastLocation(client)
                ?: requestLocationUpdate(client)

            location?.toGeoCoordinates()
                ?.takeIf { it.hasCoordinates() }
                ?.let { NativeLocationResult.Success(it) }
                ?: NativeLocationResult.Unavailable("No location fix returned.")
        } catch (error: Throwable) {
            NativeLocationResult.Unavailable(error.message)
        }
    }

    actual suspend fun isLocationEnabled(): Boolean {
        val context = NativeLocationActivityProvider.context ?: return false
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            manager.isLocationEnabled
        } else {
            manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }
    }

    actual fun hasLocationPermission(): Boolean {
        val context = NativeLocationActivityProvider.context ?: return false
        return hasPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ||
            hasPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    @SuppressLint("MissingPermission")
    private suspend fun requestCurrentLocation(
        client: com.google.android.gms.location.FusedLocationProviderClient,
    ): Location? = suspendCancellableCoroutine { continuation ->
        val cancellationToken = CancellationTokenSource()
        client.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationToken.token)
            .addOnSuccessListener { location ->
                if (continuation.isActive) continuation.resume(location)
            }
            .addOnFailureListener { error ->
                if (continuation.isActive) continuation.resume(null)
            }
            .addOnCanceledListener {
                if (continuation.isActive) continuation.resume(null)
            }

        continuation.invokeOnCancellation {
            cancellationToken.cancel()
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun requestLastLocation(
        client: com.google.android.gms.location.FusedLocationProviderClient,
    ): Location? = suspendCancellableCoroutine { continuation ->
        client.lastLocation
            .addOnSuccessListener { location ->
                if (continuation.isActive) continuation.resume(location)
            }
            .addOnFailureListener { error ->
                if (continuation.isActive) continuation.resume(null)
            }
            .addOnCanceledListener {
                if (continuation.isActive) continuation.resume(null)
            }
    }

    @SuppressLint("MissingPermission")
    private suspend fun requestLocationUpdate(
        client: com.google.android.gms.location.FusedLocationProviderClient,
    ): Location? = withTimeoutOrNull(LOCATION_UPDATE_TIMEOUT_MILLIS) {
        suspendCancellableCoroutine { continuation ->
            val request = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                LOCATION_UPDATE_INTERVAL_MILLIS
            )
                .setMaxUpdates(1)
                .build()

            val callback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    client.removeLocationUpdates(this)
                    if (continuation.isActive) {
                        continuation.resume(result.lastLocation)
                    }
                }
            }

            client.requestLocationUpdates(request, callback, Looper.getMainLooper())
                .addOnFailureListener { error ->
                    client.removeLocationUpdates(callback)
                    if (continuation.isActive) continuation.resume(null)
                }

            continuation.invokeOnCancellation {
                client.removeLocationUpdates(callback)
            }
        }
    }

    private fun hasPermission(context: Context, permission: String): Boolean =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    private fun isPermissionDeniedForever(activity: ComponentActivity): Boolean {
        val fineDeniedForever = !ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarseDeniedForever = !ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return fineDeniedForever && coarseDeniedForever
    }

    private fun Location.toGeoCoordinates(): GeoCoordinates =
        GeoCoordinates(latitude = latitude, longitude = longitude)
}

actual object NativeLocationSettings {
    actual fun openSettings() {
        val context = NativeLocationActivityProvider.context ?: return
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}

object NativeLocationActivityProvider {
    private var applicationContext: Context? = null
    var currentActivity: ComponentActivity? = null
        private set

    val context: Context?
        get() = currentActivity ?: applicationContext

    fun attach(activity: ComponentActivity) {
        currentActivity = activity
        applicationContext = activity.applicationContext
    }

    fun detach(activity: ComponentActivity) {
        if (currentActivity === activity) currentActivity = null
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray,
    ): Boolean = NativeLocationPermissionDelegate.onRequestPermissionsResult(requestCode, grantResults)
}

private object NativeLocationPermissionDelegate {
    private const val REQUEST_CODE = 8031
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private var continuation: kotlinx.coroutines.CancellableContinuation<Boolean>? = null

    suspend fun request(activity: ComponentActivity): Boolean =
        suspendCancellableCoroutine { currentContinuation ->
            if (continuation != null) {
                currentContinuation.resume(false)
                return@suspendCancellableCoroutine
            }

            continuation = currentContinuation
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE)

            currentContinuation.invokeOnCancellation {
                if (continuation === currentContinuation) {
                    continuation = null
                }
            }
        }

    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray): Boolean {
        if (requestCode != REQUEST_CODE) return false

        val granted = grantResults.any { it == PackageManager.PERMISSION_GRANTED }
        continuation?.resume(granted)
        continuation = null
        return true
    }
}

private const val LOCATION_UPDATE_INTERVAL_MILLIS = 1000L
private const val LOCATION_CURRENT_TIMEOUT_MILLIS = 5000L
private const val LOCATION_UPDATE_TIMEOUT_MILLIS = 5000L
