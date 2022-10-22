package com.kukhtoslava.weatherapp.data.repositoriesimpl

import com.kukhtoslava.weatherapp.CityDBDatabase
import com.kukhtoslava.weatherapp.data.KMMPreference
import com.kukhtoslava.weatherapp.domain.models.CurrentPlace
import com.kukhtoslava.weatherapp.domain.models.DBPlace
import com.kukhtoslava.weatherapp.domain.repositories.CacheRepository

private const val PLACE_ID_KEY = "place_id"
private const val PLACE_NAME_KEY = "place_name"

class CacheRepositoryImpl constructor(
    private val cityDBDatabase: CityDBDatabase,
    private val preference: KMMPreference
) : CacheRepository {

    override fun getAllCities(): List<DBPlace> {
        return cityDBDatabase.cityDbQueries.selectAll(mapper = { placeId, name, lat, lon ->
            DBPlace(
                placeId = placeId,
                name = name,
                lat = lat,
                lon = lon
            )
        }).executeAsList()
    }

    override fun deleteCityByName(name: String) {
        cityDBDatabase.cityDbQueries.deleteByName(name)
    }

    override fun putCity(dbPlace: DBPlace) {
        cityDBDatabase.cityDbQueries.insertItem(
            placeId = dbPlace.placeId,
            name = dbPlace.name,
            lat = dbPlace.lat,
            lon = dbPlace.lon
        )
    }

    override fun saveCurrentPlaceId(currentPlace: CurrentPlace) {
        preference.put(PLACE_ID_KEY, currentPlace.placeId)
        preference.put(PLACE_NAME_KEY, currentPlace.placeName)
    }

    override fun getCurrentPlace(): CurrentPlace? {
        val placeId = preference.getString(PLACE_ID_KEY)
        val placeName = preference.getString(PLACE_NAME_KEY)
        if (placeId == null || placeName == null) {
            return null
        }
        return CurrentPlace(placeId = placeId, placeName = placeName)
    }
}
