package com.kukhtoslava.weatherapp.domain.repositories

import com.kukhtoslava.weatherapp.domain.models.places.PlaceInfo
import com.kukhtoslava.weatherapp.domain.models.places.Predictions

interface PlaceRepository {

    suspend fun getPredictions(query: String): Predictions

    suspend fun getCityByPlaceId(placeId: String): PlaceInfo
}
