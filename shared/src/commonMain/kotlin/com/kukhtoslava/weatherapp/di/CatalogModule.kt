package com.kukhtoslava.weatherapp.di

import com.kukhtoslava.weatherapp.domain.usecases.GetCachedCitiesUseCase
import com.kukhtoslava.weatherapp.domain.usecases.GetCurrentWeatherUseCase
import com.kukhtoslava.weatherapp.domain.usecases.SaveCurrentPlaceUseCase
import org.kodein.di.*

val catalogDI = LazyDI {
    DI {
        extend(applicationDi)
        import(catalogModule)
    }
}

val catalogModule = DI.Module(CATALOG_MODULE) {

    bind<GetCurrentWeatherUseCase>() with multiton {
        GetCurrentWeatherUseCase(instance())
    }

    bind<GetCachedCitiesUseCase>() with multiton {
        GetCachedCitiesUseCase(instance())
    }

    bind<SaveCurrentPlaceUseCase>() with multiton {
        SaveCurrentPlaceUseCase(instance())
    }
}
