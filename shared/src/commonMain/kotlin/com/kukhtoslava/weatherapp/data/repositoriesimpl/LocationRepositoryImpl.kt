package com.kukhtoslava.weatherapp.data.repositoriesimpl

import com.kukhtoslava.weatherapp.data.source.LocationService
import com.kukhtoslava.weatherapp.domain.repositories.LocationRepository

class LocationRepositoryImpl(private val locationService: LocationService) : LocationRepository {

    override suspend fun getCurrentLocation() = locationService.getCurrentLocation()

    override suspend fun isLocationEnabled() = locationService.isLocationEnabled()
}
