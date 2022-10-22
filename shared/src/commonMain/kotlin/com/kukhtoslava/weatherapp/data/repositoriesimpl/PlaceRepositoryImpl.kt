package com.kukhtoslava.weatherapp.data.repositoriesimpl

import com.kukhtoslava.weatherapp.data.source.PlaceApiService
import com.kukhtoslava.weatherapp.domain.repositories.PlaceRepository

class PlaceRepositoryImpl constructor(private val placeApiService: PlaceApiService) :
    PlaceRepository {
    override suspend fun getPredictions(query: String) = placeApiService.getPredictions(query)

    override suspend fun getCityByPlaceId(placeId: String) =
        placeApiService.getCityByPlaceId(placeId)
}
