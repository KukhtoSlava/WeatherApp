package com.kukhtoslava.weatherapp.presentation.search

import com.kukhtoslava.weatherapp.domain.models.AppError
import com.kukhtoslava.weatherapp.domain.models.places.Prediction

sealed interface SearchAction {

    fun reduce(state: SearchState): SearchState

    data class Search(val term: String) : SearchAction {
        override fun reduce(state: SearchState): SearchState {
            return state.copy(
                isLoading = true
            )
        }
    }

    data class ClickCity(val placeName: String, val placeId: String) : SearchAction {
        override fun reduce(state: SearchState) = state
    }

    object CloseScreen : SearchAction {
        override fun reduce(state: SearchState) = state
    }

    object CheckCurrentPlace : SearchAction {
        override fun reduce(state: SearchState) = state
    }

    object Clear : SearchAction {
        override fun reduce(state: SearchState): SearchState {
            return state.copy(
                predictions = emptyList(),
                term = "",
                isLoading = false,
            )
        }
    }
}

internal sealed interface SideEffectAction : SearchAction {

    data class TextChanged(val term: String) : SideEffectAction {
        override fun reduce(state: SearchState) = state
    }

    data class CurrentPlaceChecked(val isExist: Boolean) : SideEffectAction {
        override fun reduce(state: SearchState): SearchState {
            return state.copy(
                isExistCurrentPlace = isExist
            )
        }
    }

    object CityChanged : SideEffectAction {
        override fun reduce(state: SearchState) = state
    }

    data class SearchResult(
        val predictions: List<Prediction> = listOf(),
        val term: String,
        val appError: AppError? = null
    ) : SideEffectAction {
        override fun reduce(state: SearchState): SearchState {
            return state.copy(
                predictions = predictions,
                term = term,
                isLoading = false,
                error = appError
            )
        }
    }
}

internal fun SearchAction.toSearchSingleEventOrNull(): SearchEvent? =
    when (this) {
        is SearchAction.Search -> null
        is SearchAction.Clear -> null
        is SearchAction.ClickCity -> null
        is SearchAction.CheckCurrentPlace -> null
        is SearchAction.CloseScreen -> SearchEvent.Close
        is SideEffectAction.SearchResult -> null
        is SideEffectAction.TextChanged -> null
        is SideEffectAction.CityChanged -> SearchEvent.Close
        is SideEffectAction.CurrentPlaceChecked -> null
    }
