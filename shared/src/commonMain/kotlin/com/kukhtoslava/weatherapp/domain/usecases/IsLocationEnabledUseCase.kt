package com.kukhtoslava.weatherapp.domain.usecases

import com.kukhtoslava.weatherapp.domain.repositories.LocationRepository

class IsLocationEnabledUseCase(private val locationRepository: LocationRepository) {

    suspend operator fun invoke() =
        locationRepository.isLocationEnabled()
}
