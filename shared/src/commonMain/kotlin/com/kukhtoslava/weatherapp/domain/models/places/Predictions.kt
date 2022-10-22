package com.kukhtoslava.weatherapp.domain.models.places

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Predictions(
    @SerialName("predictions")
    val predictions: List<Prediction>
)
