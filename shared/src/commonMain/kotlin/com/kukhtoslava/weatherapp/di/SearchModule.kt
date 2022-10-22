package com.kukhtoslava.weatherapp.di

import com.kukhtoslava.weatherapp.domain.usecases.GetCurrentPlaceUseCase
import com.kukhtoslava.weatherapp.domain.usecases.GetPredictionsUseCase
import com.kukhtoslava.weatherapp.domain.usecases.SaveCurrentPlaceUseCase
import org.kodein.di.*

val searchDI = LazyDI {
    DI {
        extend(applicationDi)
        import(searchModule)
    }
}

val searchModule = DI.Module(SEARCH_MODULE) {

    bind<GetPredictionsUseCase>() with multiton {
        GetPredictionsUseCase(instance())
    }

    bind<SaveCurrentPlaceUseCase>() with multiton {
        SaveCurrentPlaceUseCase(instance())
    }

    bind<GetCurrentPlaceUseCase>() with multiton {
        GetCurrentPlaceUseCase(instance())
    }
}
