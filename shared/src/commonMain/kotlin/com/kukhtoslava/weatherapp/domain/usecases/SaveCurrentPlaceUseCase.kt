package com.kukhtoslava.weatherapp.domain.usecases

import com.kukhtoslava.weatherapp.domain.models.CurrentPlace
import com.kukhtoslava.weatherapp.domain.repositories.CacheRepository

class SaveCurrentPlaceUseCase(private val cacheRepository: CacheRepository) {

    suspend operator fun invoke(placeName: String, placeId: String) {
        val currentPlace = CurrentPlace(
            placeName = placeName,
            placeId = placeId
        )
        cacheRepository.saveCurrentPlaceId(currentPlace = currentPlace)
    }
}
