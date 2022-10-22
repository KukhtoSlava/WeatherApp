package com.kukhtoslava.weatherapp.presentation.search

sealed interface SearchEvent {
    object Close : SearchEvent
}
