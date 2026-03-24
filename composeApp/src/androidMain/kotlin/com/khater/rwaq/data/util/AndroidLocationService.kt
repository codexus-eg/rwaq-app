//package com.khater.rwaq.data.util
//
//import android.annotation.SuppressLint
//import android.content.Context
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.location.Priority
//import kotlinx.coroutines.tasks.await
//
//// You need to pass a Context. In KMP, you typically use a Dependency Injection framework
//// (like Koin) or pass the context to the constructor from the Android Activity.
//// For this example, we assume you can access the Application Context.
//class AndroidLocationService(private val context: Context) {
//    private val client = LocationServices.getFusedLocationProviderClient(context)
//
//    @SuppressLint("MissingPermission") // Ensure you ask for permissions before calling this!
//    suspend fun getCurrentLocation(): GeoLocation? {
//        return try {
//            val location = client.getCurrentLocation(
//                Priority.PRIORITY_HIGH_ACCURACY,
//                null
//            ).await()
//
//            location?.let { GeoLocation(it.latitude, it.longitude) }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }
//}
//
//// Actual implementation mapping
//actual class LocationService(private val context: Context) {
//    private val delegate = AndroidLocationService(context)
//
//    actual suspend fun getCurrentLocation(): GeoLocation? {
//        return delegate.getCurrentLocation()
//    }
//}