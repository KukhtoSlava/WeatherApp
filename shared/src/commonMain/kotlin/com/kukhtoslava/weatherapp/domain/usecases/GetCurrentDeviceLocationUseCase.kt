package com.kukhtoslava.weatherapp.domain.usecases

import com.kukhtoslava.weatherapp.domain.repositories.LocationRepository

class GetCurrentDeviceLocationUseCase(private val locationRepository: LocationRepository) {

    suspend operator fun invoke() =
        locationRepository.getCurrentLocation()
}
