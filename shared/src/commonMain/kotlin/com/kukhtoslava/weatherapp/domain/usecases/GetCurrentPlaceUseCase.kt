package com.kukhtoslava.weatherapp.domain.usecases

import com.kukhtoslava.weatherapp.domain.repositories.CacheRepository

class GetCurrentPlaceUseCase(private val cacheRepository: CacheRepository) {

    suspend operator fun invoke() =
        cacheRepository.getCurrentPlace()
}
