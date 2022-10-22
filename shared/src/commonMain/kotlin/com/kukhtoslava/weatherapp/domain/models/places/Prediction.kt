package com.kukhtoslava.weatherapp.domain.models.places

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Prediction(
    @SerialName("description")
    val description: String,
    @SerialName("place_id")
    val placeId: String
)
