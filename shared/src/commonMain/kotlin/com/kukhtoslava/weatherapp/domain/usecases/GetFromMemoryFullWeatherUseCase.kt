package com.kukhtoslava.weatherapp.domain.usecases

import com.kukhtoslava.weatherapp.domain.models.weather.FullWeather
import com.kukhtoslava.weatherapp.domain.repositories.InMemoryRepository

class GetFromMemoryFullWeatherUseCase(private val inMemoryRepository: InMemoryRepository) {

    suspend operator fun invoke(placeId: String): FullWeather? {
        return inMemoryRepository.getFullWeatherByPlaceId(placeId)
    }
}
