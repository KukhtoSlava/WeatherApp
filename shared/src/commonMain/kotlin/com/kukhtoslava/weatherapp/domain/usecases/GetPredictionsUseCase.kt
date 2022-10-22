package com.kukhtoslava.weatherapp.domain.usecases

import com.kukhtoslava.weatherapp.domain.repositories.PlaceRepository

class GetPredictionsUseCase(private val placeRepository: PlaceRepository) {

    suspend operator fun invoke(query: String) = placeRepository.getPredictions(query)
}
