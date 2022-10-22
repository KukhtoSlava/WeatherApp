package com.kukhtoslava.weatherapp.data.repositoriesimpl

import com.kukhtoslava.weatherapp.data.source.WeatherApiService
import com.kukhtoslava.weatherapp.domain.repositories.WeatherRepository

class WeatherRepositoryImpl(private val weatherApiService: WeatherApiService) : WeatherRepository {

    override suspend fun getFullWeather(lat: Double, lon: Double) =
        weatherApiService.getFullWeather(lat = lat, lon = lon)

    override suspend fun getCurrentWeather(lat: Double, lon: Double) =
        weatherApiService.getCurrentWeather(lat = lat, lon = lon)
}
