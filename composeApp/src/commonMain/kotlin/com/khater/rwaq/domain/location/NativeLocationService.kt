package com.khater.rwaq.domain.location

data class GeoCoordinates(
    val latitude: Double,
    val longitude: Double,
) {
    fun hasCoordinates(): Boolean = latitude != 0.0 || longitude != 0.0
}

sealed interface NativeLocationResult {
    data class Success(val coordinates: GeoCoordinates) : NativeLocationResult
    data object ServicesDisabled : NativeLocationResult
    data class PermissionDenied(val forever: Boolean) : NativeLocationResult
    data class Unavailable(val message: String? = null) : NativeLocationResult
}

expect class NativeLocationService() {
    suspend fun getCurrentLocation(): NativeLocationResult
    suspend fun isLocationEnabled(): Boolean
    fun hasLocationPermission(): Boolean
}

expect object NativeLocationSettings {
    fun openSettings()
}
