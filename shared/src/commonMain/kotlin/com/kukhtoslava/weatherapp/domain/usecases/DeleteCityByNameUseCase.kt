package com.kukhtoslava.weatherapp.domain.usecases

import com.kukhtoslava.weatherapp.domain.repositories.CacheRepository

class DeleteCityByNameUseCase(private val cacheRepository: CacheRepository) {

    suspend operator fun invoke(name: String) = cacheRepository.deleteCityByName(name)
}
