package com.kukhtoslava.weatherapp.data.source

import com.kukhtoslava.weatherapp.PLACE_APPLICATION_KEY
import com.kukhtoslava.weatherapp.domain.models.places.PlaceInfo
import com.kukhtoslava.weatherapp.domain.models.places.Predictions
import com.kukhtoslava.weatherapp.utils.AppCoroutineDispatchers
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.withContext

interface PlaceApiService {

    suspend fun getPredictions(query: String): Predictions

    suspend fun getCityByPlaceId(placeId: String): PlaceInfo
}

class PlaceApiServiceImpl(
    private val httpClient: HttpClient, private val appCoroutineDispatchers: AppCoroutineDispatchers
) : PlaceApiService {

    override suspend fun getPredictions(query: String): Predictions =
        withContext(appCoroutineDispatchers.io) {
            httpClient.get(URLBuilder(PLACE_BASE_URL).apply {
                path("maps/api/place/autocomplete/json")
                parameters.append(KEY_APP, PLACE_APP_KEY)
                parameters.append(KEY_TYPES, VALUE_TYPES)
                parameters.append(KEY_LANGUAGE, VALUE_LANGUAGE)
                parameters.append(KEY_INPUT, query)
            }.build()).body()
        }

    override suspend fun getCityByPlaceId(placeId: String): PlaceInfo =
        withContext(appCoroutineDispatchers.io) {
            httpClient.get(URLBuilder(PLACE_BASE_URL).apply {
                path("maps/api/place/details/json")
                parameters.append(KEY_APP, PLACE_APP_KEY)
                parameters.append(KEY_PLACE_ID, placeId)
                parameters.append(KEY_LANGUAGE, VALUE_LANGUAGE)
            }.build()).body()
        }

    private companion object {

        private const val PLACE_APP_KEY = PLACE_APPLICATION_KEY
        private const val PLACE_BASE_URL = "https://maps.googleapis.com/"
        private const val KEY_INPUT = "input"
        private const val KEY_TYPES = "types"
        private const val VALUE_TYPES = "(cities)"
        private const val KEY_LANGUAGE = "language"
        private const val VALUE_LANGUAGE = "en"
        private const val KEY_APP = "key"
        private const val KEY_PLACE_ID = "place_id"
    }
}
