package com.kukhtoslava.weatherapp.data.source

import com.kukhtoslava.weatherapp.domain.models.places.Location

interface LocationService {

    suspend fun getCurrentLocation(): Location

    suspend fun isLocationEnabled(): Boolean
}
