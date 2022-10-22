package com.kukhtoslava.weatherapp.domain.usecases

import com.kukhtoslava.weatherapp.domain.repositories.PlaceRepository

class GetCityByPlaceIdUseCase(private val placeRepository: PlaceRepository) {

    suspend operator fun invoke(placeId: String) = placeRepository.getCityByPlaceId(placeId)
}
