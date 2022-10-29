package com.kukhtoslava.weatherapp.di

import com.kukhtoslava.weatherapp.domain.usecases.*
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

    bind<GetCityByLocationUseCase>() with multiton {
        GetCityByLocationUseCase(instance())
    }

    bind<GetCurrentDeviceLocationUseCase>() with multiton {
        GetCurrentDeviceLocationUseCase(instance())
    }

    bind<IsLocationEnabledUseCase>() with multiton {
        IsLocationEnabledUseCase(instance())
    }
}
