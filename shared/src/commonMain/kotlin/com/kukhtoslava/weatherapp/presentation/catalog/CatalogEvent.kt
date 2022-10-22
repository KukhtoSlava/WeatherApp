package com.kukhtoslava.weatherapp.presentation.catalog

sealed interface CatalogEvent {
    object Close : CatalogEvent
}
