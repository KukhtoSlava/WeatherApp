package com.kukhtoslava.weatherapp.domain.repositories

import com.kukhtoslava.weatherapp.domain.models.weather.FullWeather

interface InMemoryRepository {

    fun saveFullWeather(placeId: String, fullWeather: FullWeather)

    fun getFullWeatherByPlaceId(placeId: String): FullWeather?
}
