package com.kukhtoslava.weatherapp.data.repositoriesimpl

import com.kukhtoslava.weatherapp.domain.models.weather.FullWeather
import com.kukhtoslava.weatherapp.domain.repositories.InMemoryRepository

class InMemoryRepositoryImpl : InMemoryRepository {

    private val inMemoryMap: MutableMap<String, FullWeather> = HashMap()

    override fun saveFullWeather(placeId: String, fullWeather: FullWeather) {
        inMemoryMap[placeId] = fullWeather
    }

    override fun getFullWeatherByPlaceId(placeId: String): FullWeather? {
        return inMemoryMap[placeId]
    }
}
