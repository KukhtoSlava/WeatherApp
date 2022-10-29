package com.kukhtoslava.weatherapp.domain.repositories

import com.kukhtoslava.weatherapp.domain.models.places.Location

interface LocationRepository {

    suspend fun getCurrentLocation(): Location

    suspend fun isLocationEnabled(): Boolean
}
