//package com.khater.rwaq.data.util
//
//import dev.jordond.compass.Priority
//import dev.jordond.compass.geocoder.Geocoder
//import dev.jordond.compass.geocoder.mobile
//import dev.jordond.compass.geolocation.Geolocator
//import dev.jordond.compass.geolocation.mobile
//
//class LocationService {
//
//    private val geolocator: Geolocator = Geolocator.mobile()
//    private val geocoder: Geocoder = Geocoder.mobile()
//
//    suspend fun getCurrentAddress(): Result<String> {
//        // 1. Get the current location (returns Result<Location>)
//        val locationResult = geolocator.current(priority = Priority.HighAccuracy)
//
//        // 2. Unwrap the location safely
//        val location = locationResult.getOrNull()
//            ?: return Result.failure(Exception("Location not found"))
//
//        // 3. Get the address list (returns Result<List<Place>>)
//        val geocodeResult = geocoder.reverse(location.coordinates)
//
//        // 4. Unwrap the list of places
//        val places = geocodeResult.getOrNull()
//
//        return if (!places.isNullOrEmpty()) {
//            // Get the first match
//            val place = places[0]
//
//            // 5. Build the address string using YOUR specific fields
//            val formattedAddress = buildString {
//                // 'street' is available directly
//                place.street?.let { append(it).append(", ") }
//
//                // 'locality' is the City
//                place.locality?.let { append(it).append(", ") }
//
//                // 'administrativeArea' is the State/Province
//                place.administrativeArea?.let { append(it).append(" ") }
//
//                place.postalCode?.let { append(it).append(" ") }
//
//                place.country?.let { append(it) }
//            }.removeSuffix(", ").trim()
//
//            Result.success(formattedAddress)
//        } else {
//            Result.failure(Exception("Address not found"))
//        }
//    }
//}