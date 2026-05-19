package com.khater.rwaq.domain.location

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.Foundation.NSError
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import platform.darwin.NSObject
import kotlin.coroutines.resume

actual class NativeLocationService actual constructor() {
    private val delegate = IosLocationDelegate()

    actual suspend fun getCurrentLocation(): NativeLocationResult {
        return when (delegate.ensurePermission()) {
            IosPermissionState.Granted -> {
                if (!isLocationEnabled()) return NativeLocationResult.ServicesDisabled

                val location = withTimeoutOrNull(LOCATION_CURRENT_TIMEOUT_MILLIS) {
                    delegate.requestCurrentLocation()
                }
                    ?: delegate.lastKnownLocation()
                    ?: delegate.requestTrackedLocation()

                location?.toGeoCoordinates()
                    ?.takeIf { it.hasCoordinates() }
                    ?.let { NativeLocationResult.Success(it) }
                    ?: NativeLocationResult.Unavailable("No location fix returned.")
            }
            IosPermissionState.DeniedForever -> NativeLocationResult.PermissionDenied(forever = true)
            IosPermissionState.NotDetermined -> NativeLocationResult.PermissionDenied(forever = false)
        }
    }

    actual suspend fun isLocationEnabled(): Boolean =
        withContext(Dispatchers.Default) {
            CLLocationManager.locationServicesEnabled()
        }

    actual fun hasLocationPermission(): Boolean =
        delegate.permissionState() == IosPermissionState.Granted
}

actual object NativeLocationSettings {
    actual fun openSettings() {
        UIApplication.sharedApplication().openURL(
            url = NSURL(string = UIApplicationOpenSettingsURLString),
            options = emptyMap<Any?, Any>(),
            completionHandler = null
        )
    }
}

private enum class IosPermissionState {
    Granted,
    DeniedForever,
    NotDetermined,
}

private sealed interface IosLocationResponse {
    data class Success(val location: CLLocation) : IosLocationResponse
    data class Failure(val message: String?) : IosLocationResponse
}

private class IosLocationDelegate : NSObject(), CLLocationManagerDelegateProtocol {
    private val manager = CLLocationManager()
    private var permissionContinuation: CancellableContinuation<CLAuthorizationStatus>? = null
    private var currentLocationContinuation: CancellableContinuation<IosLocationResponse>? = null
    private var trackingLocationContinuation: CancellableContinuation<IosLocationResponse>? = null

    init {
        manager.delegate = this
        manager.desiredAccuracy = kCLLocationAccuracyBest
    }

    fun permissionState(): IosPermissionState =
        manager.authorizationStatus.toPermissionState()

    suspend fun ensurePermission(): IosPermissionState {
        val currentState = permissionState()
        if (currentState != IosPermissionState.NotDetermined) return currentState

        val status = suspendCancellableCoroutine { continuation ->
            permissionContinuation = continuation
            manager.requestWhenInUseAuthorization()
            continuation.invokeOnCancellation {
                if (permissionContinuation === continuation) {
                    permissionContinuation = null
                }
            }
        }

        return status.toPermissionState()
    }

    fun lastKnownLocation(): CLLocation? = manager.location

    suspend fun requestCurrentLocation(): CLLocation? {
        val response = suspendCancellableCoroutine { continuation ->
            currentLocationContinuation = continuation
            manager.requestLocation()
            continuation.invokeOnCancellation {
                if (currentLocationContinuation === continuation) {
                    currentLocationContinuation = null
                }
            }
        }

        return (response as? IosLocationResponse.Success)?.location
    }

    suspend fun requestTrackedLocation(): CLLocation? =
        withTimeoutOrNull(LOCATION_UPDATE_TIMEOUT_MILLIS) {
            val response = suspendCancellableCoroutine { continuation ->
                trackingLocationContinuation = continuation
                manager.startUpdatingLocation()
                continuation.invokeOnCancellation {
                    manager.stopUpdatingLocation()
                    if (trackingLocationContinuation === continuation) {
                        trackingLocationContinuation = null
                    }
                }
            }
            (response as? IosLocationResponse.Success)?.location
        }

    override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
        permissionContinuation?.let { continuation ->
            if (continuation.isActive) {
                continuation.resume(manager.authorizationStatus)
            }
            permissionContinuation = null
        }
    }

    override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
        @Suppress("UNCHECKED_CAST")
        val locations = didUpdateLocations as? List<CLLocation>
        val location = locations?.lastOrNull()

        if (location != null) {
            currentLocationContinuation?.let { continuation ->
                if (continuation.isActive) {
                    continuation.resume(IosLocationResponse.Success(location))
                }
                currentLocationContinuation = null
            }

            trackingLocationContinuation?.let { continuation ->
                manager.stopUpdatingLocation()
                if (continuation.isActive) {
                    continuation.resume(IosLocationResponse.Success(location))
                }
                trackingLocationContinuation = null
            }
        }
    }

    override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
        val response = IosLocationResponse.Failure(didFailWithError.localizedDescription)

        currentLocationContinuation?.let { continuation ->
            if (continuation.isActive) {
                continuation.resume(response)
            }
            currentLocationContinuation = null
        }

        trackingLocationContinuation?.let { continuation ->
            manager.stopUpdatingLocation()
            if (continuation.isActive) {
                continuation.resume(response)
            }
            trackingLocationContinuation = null
        }
    }
}

private fun CLAuthorizationStatus.toPermissionState(): IosPermissionState =
    when (this) {
        kCLAuthorizationStatusAuthorizedAlways,
        kCLAuthorizationStatusAuthorizedWhenInUse -> IosPermissionState.Granted
        kCLAuthorizationStatusDenied,
        kCLAuthorizationStatusRestricted -> IosPermissionState.DeniedForever
        kCLAuthorizationStatusNotDetermined -> IosPermissionState.NotDetermined
        else -> IosPermissionState.NotDetermined
    }

@OptIn(ExperimentalForeignApi::class)
private fun CLLocation.toGeoCoordinates(): GeoCoordinates =
    coordinate().useContents {
        GeoCoordinates(latitude = latitude, longitude = longitude)
    }

private const val LOCATION_UPDATE_TIMEOUT_MILLIS = 5000L
private const val LOCATION_CURRENT_TIMEOUT_MILLIS = 5000L
