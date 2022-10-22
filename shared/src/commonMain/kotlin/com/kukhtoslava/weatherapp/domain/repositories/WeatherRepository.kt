package com.kukhtoslava.weatherapp.domain.repositories

import com.kukhtoslava.weatherapp.domain.models.weather.CurrentWeather
import com.kukhtoslava.weatherapp.domain.models.weather.FullWeather

interface WeatherRepository {

    suspend fun getFullWeather(lat: Double, lon: Double): FullWeather

    suspend fun getCurrentWeather(lat: Double, lon: Double): CurrentWeather
}
