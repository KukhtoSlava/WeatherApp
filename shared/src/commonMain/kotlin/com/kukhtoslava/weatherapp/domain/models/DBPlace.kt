package com.kukhtoslava.weatherapp.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class DBPlace(
    val placeId: String,
    val name: String,
    val lat: Double,
    val lon: Double
)
