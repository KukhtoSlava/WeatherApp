package com.kukhtoslava.weatherapp.domain.models.places

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Location(
    @SerialName("lat")
    val lat: Double,
    @SerialName("lng")
    val lng: Double
)
