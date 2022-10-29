package com.kukhtoslava.weatherapp.domain.models.places

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Result(
    @SerialName("geometry")
    val geometry: Geometry,
    @SerialName("name")
    val name: String,
    @SerialName("formatted_address")
    val formattedAddress: String? = null,
    @SerialName("place_id")
    val placeId: String
)
