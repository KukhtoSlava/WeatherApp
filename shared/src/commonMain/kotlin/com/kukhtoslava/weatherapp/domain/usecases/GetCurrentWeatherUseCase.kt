package com.kukhtoslava.weatherapp.domain.usecases

import com.kukhtoslava.weatherapp.domain.repositories.WeatherRepository

class GetCurrentWeatherUseCase(private val weatherRepository: WeatherRepository) {

    suspend operator fun invoke(lat: Double, lon: Double) =
        weatherRepository.getCurrentWeather(lat = lat, lon = lon)
}
