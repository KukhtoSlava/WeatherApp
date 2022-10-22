package com.kukhtoslava.weatherapp.domain.usecases

import com.kukhtoslava.weatherapp.domain.models.weather.FullWeather
import com.kukhtoslava.weatherapp.domain.repositories.InMemoryRepository

class SaveInMemoryFullWeatherUseCase(private val inMemoryRepository: InMemoryRepository) {

    suspend operator fun invoke(placeId: String, fullWeather: FullWeather) {
        inMemoryRepository.saveFullWeather(placeId = placeId, fullWeather = fullWeather)
    }
}
