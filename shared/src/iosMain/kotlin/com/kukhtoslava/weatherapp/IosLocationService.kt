package com.kukhtoslava.weatherapp

import com.kukhtoslava.weatherapp.data.source.LocationService
import com.kukhtoslava.weatherapp.domain.models.AppError
import com.kukhtoslava.weatherapp.domain.models.places.Location
import kotlinx.cinterop.CValue
import kotlinx.cinterop.useContents
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.CLLocationCoordinate2D
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.resumeWithException

class IosLocationService : LocationService {

    private val locationManager = CLLocationManager()

    override suspend fun getCurrentLocation(): Location {
        return suspendCancellableCoroutine { continuation ->
            locationManager.delegate = CLLocationManagerDelegate(
                success = { lat, lon ->
                    val location = Location(
                        lat = lat,
                        lng = lon
                    )
                    locationManager.stopUpdatingLocation()
                    continuation.resume(location) {}
                },
                error = { throwable ->
                    continuation.resumeWithException(throwable)
                }
            )
            locationManager.desiredAccuracy = kCLLocationAccuracyBest
            locationManager.requestAlwaysAuthorization()
            locationManager.startUpdatingLocation()
        }
    }

    override suspend fun isLocationEnabled(): Boolean {
        return locationManager.locationServicesEnabled
    }
}

class CLLocationManagerDelegate(
    val success: (lat: Double, lon: Double) -> Unit,
    val error: (Throwable) -> Unit
) : NSObject(), CLLocationManagerDelegateProtocol {

    override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
        val locValue: CValue<CLLocationCoordinate2D>? = manager.location?.coordinate
        locValue?.useContents {
            success(latitude, longitude)
        } ?: run {
            error(
                AppError.LocationException.UnknownException(
                    Throwable("Can't get location")
                )
            )
        }
    }

    override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
        error(
            AppError.LocationException.UnknownException(
                Throwable("Can't get location")
            )
        )
    }
}
