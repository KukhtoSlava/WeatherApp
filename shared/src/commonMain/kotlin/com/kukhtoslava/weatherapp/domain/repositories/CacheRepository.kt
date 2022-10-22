package com.kukhtoslava.weatherapp.domain.repositories

import com.kukhtoslava.weatherapp.domain.models.CurrentPlace
import com.kukhtoslava.weatherapp.domain.models.DBPlace

interface CacheRepository {

    fun getAllCities(): List<DBPlace>

    fun deleteCityByName(name: String)

    fun putCity(dbPlace: DBPlace)

    fun saveCurrentPlaceId(currentPlace: CurrentPlace)

    fun getCurrentPlace(): CurrentPlace?
}
