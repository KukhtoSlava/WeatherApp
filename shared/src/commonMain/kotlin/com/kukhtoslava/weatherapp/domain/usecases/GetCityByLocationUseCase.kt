package com.kukhtoslava.weatherapp.domain.usecases

import com.kukhtoslava.weatherapp.domain.repositories.PlaceRepository

class GetCityByLocationUseCase(private val placeRepository: PlaceRepository) {

    suspend operator fun invoke(lat: Double, lon: Double) =
        placeRepository.getCityByLocation(lat = lat, lon = lon)
}
