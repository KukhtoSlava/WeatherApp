package com.kukhtoslava.weatherapp.domain.models.places

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationInfo(
    @SerialName("results")
    val results: List<Result>
)
