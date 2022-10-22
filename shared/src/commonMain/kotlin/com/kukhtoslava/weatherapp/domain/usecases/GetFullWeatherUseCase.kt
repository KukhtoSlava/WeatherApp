package com.kukhtoslava.weatherapp.domain.usecases

import com.kukhtoslava.weatherapp.domain.repositories.WeatherRepository

class GetFullWeatherUseCase(private val weatherRepository: WeatherRepository) {

    suspend operator fun invoke(lat: Double, lon: Double) =
        weatherRepository.getFullWeather(lat = lat, lon = lon)
}
