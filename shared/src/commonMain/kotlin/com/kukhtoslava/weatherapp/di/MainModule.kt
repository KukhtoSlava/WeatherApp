package com.kukhtoslava.weatherapp.di

import com.kukhtoslava.weatherapp.domain.usecases.*
import org.kodein.di.*

val mainDI = LazyDI {
    DI {
        extend(applicationDi)
        import(mainModule)
    }
}

val mainModule = DI.Module(MAIN_MODULE) {

    bind<GetCityByPlaceIdUseCase>() with multiton {
        GetCityByPlaceIdUseCase(instance())
    }

    bind<GetFullWeatherUseCase>() with multiton {
        GetFullWeatherUseCase(instance())
    }

    bind<GetCurrentPlaceUseCase>() with multiton {
        GetCurrentPlaceUseCase(instance())
    }

    bind<SaveInMemoryFullWeatherUseCase>() with multiton {
        SaveInMemoryFullWeatherUseCase(instance())
    }

    bind<SaveCityUseCase>() with multiton {
        SaveCityUseCase(instance())
    }
}
