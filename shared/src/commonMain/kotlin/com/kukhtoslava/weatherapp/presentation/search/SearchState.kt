package com.kukhtoslava.weatherapp.presentation.search

import com.kukhtoslava.weatherapp.domain.models.AppError
import com.kukhtoslava.weatherapp.domain.models.places.Prediction

data class SearchState(
    val isExistCurrentPlace: Boolean,
    val term: String,
    val predictions: List<Prediction>,
    val isLoading: Boolean,
    val error: AppError?,
) {

    companion object {

        fun initial(): SearchState = SearchState(
            isExistCurrentPlace = false,
            term = "",
            predictions = listOf(),
            isLoading = false,
            error = null
        )
    }
}
