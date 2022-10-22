package com.kukhtoslava.weatherapp.domain.models.weather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Hourly(
    @SerialName("dt")
    val dt: Int,
    @SerialName("temp")
    val temp: Double,
    @SerialName("weather")
    val weather: List<Weather>,
)
