package com.kukhtoslava.weatherapp.di

import com.kukhtoslava.weatherapp.domain.usecases.GetCurrentPlaceUseCase
import com.kukhtoslava.weatherapp.domain.usecases.GetFromMemoryFullWeatherUseCase
import org.kodein.di.*

val descriptionDI = LazyDI {
    DI {
        extend(applicationDi)
        import(descriptionModule)
    }
}

val descriptionModule = DI.Module(DESCRIPTION_MODULE) {

    bind<GetCurrentPlaceUseCase>() with multiton {
        GetCurrentPlaceUseCase(instance())
    }

    bind<GetFromMemoryFullWeatherUseCase>() with multiton {
        GetFromMemoryFullWeatherUseCase(instance())
    }
}
