package com.kukhtoslava.weatherapp.presentation.main

sealed interface MainEvent {
    object NavigationToDescription : MainEvent
    object NavigationToCatalog : MainEvent
    object NavigationToSearch : MainEvent
}
