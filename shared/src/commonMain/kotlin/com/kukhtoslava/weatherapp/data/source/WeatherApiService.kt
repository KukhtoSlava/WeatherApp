package com.kukhtoslava.weatherapp.data.source

import com.kukhtoslava.weatherapp.OPEN_WEATHER_APPLICATION_ID
import com.kukhtoslava.weatherapp.domain.models.weather.CurrentWeather
import com.kukhtoslava.weatherapp.domain.models.weather.FullWeather
import com.kukhtoslava.weatherapp.utils.AppCoroutineDispatchers
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.withContext

interface WeatherApiService {

    suspend fun getFullWeather(lat: Double, lon: Double): FullWeather

    suspend fun getCurrentWeather(lat: Double, lon: Double): CurrentWeather
}

class WeatherApiServiceImpl(
    private val httpClient: HttpClient, private val appCoroutineDispatchers: AppCoroutineDispatchers
) : WeatherApiService {

    override suspend fun getFullWeather(lat: Double, lon: Double): FullWeather =
        withContext(appCoroutineDispatchers.io) {
            httpClient.get(URLBuilder(WEATHER_BASE_URL).apply {
                path("data/2.5/onecall")
                parameters.append(KEY_APP_ID, OPEN_WEATHER_APP_ID)
                parameters.append(KEY_UNITS, VALUE_UNITS)
                parameters.append(KEY_LAT, lat.toString())
                parameters.append(KEY_LON, lon.toString())
                parameters.append(KEY_LANGUAGE, VALUE_LANGUAGE)
            }.build()).body()
        }

    override suspend fun getCurrentWeather(lat: Double, lon: Double): CurrentWeather =
        withContext(appCoroutineDispatchers.io) {
            httpClient.get(URLBuilder(WEATHER_BASE_URL).apply {
                path("data/2.5/weather")
                parameters.append(KEY_APP_ID, OPEN_WEATHER_APP_ID)
                parameters.append(KEY_UNITS, VALUE_UNITS)
                parameters.append(KEY_LAT, lat.toString())
                parameters.append(KEY_LON, lon.toString())
                parameters.append(KEY_LANGUAGE, VALUE_LANGUAGE)
            }.build()).body()
        }

    private companion object {

        private const val OPEN_WEATHER_APP_ID = OPEN_WEATHER_APPLICATION_ID
        private const val WEATHER_BASE_URL = "https://api.openweathermap.org/"
        private const val KEY_LAT = "lat"
        private const val KEY_LON = "lon"
        private const val KEY_APP_ID = "appId"
        private const val KEY_UNITS = "units"
        private const val VALUE_UNITS = "metric"
        private const val KEY_LANGUAGE = "lang"
        private const val VALUE_LANGUAGE = "en"
    }
}
