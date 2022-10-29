package com.kukhtoslava.weatherapp.presentation.search

sealed interface SearchEvent {
    object Close : SearchEvent
    object DeniedMessage : SearchEvent
    object DeniedAlwaysMessage : SearchEvent
    object DisabledMessage : SearchEvent
    class ErrorMessage(val message: String?) : SearchEvent
}
