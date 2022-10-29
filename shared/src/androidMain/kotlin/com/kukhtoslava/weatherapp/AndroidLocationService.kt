package com.kukhtoslava.weatherapp

import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import com.kukhtoslava.weatherapp.data.source.LocationService
import com.kukhtoslava.weatherapp.domain.models.AppError
import com.kukhtoslava.weatherapp.domain.models.places.Location
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

private const val MIN_DISTANCE_M = 0F
private const val MIN_TIME_MS = 5000L

class AndroidLocationService(context: Context) : LocationService {

    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var locationListener: LocationListener? = null

    override suspend fun getCurrentLocation(): Location {
        return suspendCancellableCoroutine { continuation ->
            if (locationManager.isLocationEnabled) {
                val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                locationListener = LocationListener {
                    val location = Location(
                        lat = it.latitude,
                        lng = it.longitude
                    )
                    continuation.resume(location) { // do nothing
                    }
                    locationListener?.let { listener ->
                        locationManager.removeUpdates(listener)
                    }
                }
                if (hasGps) {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_MS,
                        MIN_DISTANCE_M,
                        locationListener!!
                    )
                } else if (hasNetwork) {
                    locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_MS,
                        MIN_DISTANCE_M,
                        locationListener!!
                    )
                } else {
                    continuation.resumeWithException(
                        AppError.LocationException.UnknownException(
                            Throwable("Can't get location")
                        )
                    )
                }
            } else {
                continuation.resumeWithException(
                    AppError.LocationException.LocationNotEnabledException(
                        IllegalAccessException("Location is Denied")
                    )
                )
            }
        }
    }

    override suspend fun isLocationEnabled(): Boolean {
        return locationManager.isLocationEnabled
    }
}
