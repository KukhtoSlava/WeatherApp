package com.kukhtoslava.weatherapp.domain.usecases

import com.kukhtoslava.weatherapp.domain.models.DBPlace
import com.kukhtoslava.weatherapp.domain.repositories.CacheRepository

class SaveCityUseCase(private val cacheRepository: CacheRepository) {

    suspend operator fun invoke(dbPlace: DBPlace) =
        cacheRepository.putCity(dbPlace)
}
