package com.kukhtoslava.weatherapp.di

import com.kukhtoslava.weatherapp.*
import com.kukhtoslava.weatherapp.data.AppErrorMapper
import com.kukhtoslava.weatherapp.data.AppErrorMapperImpl
import com.kukhtoslava.weatherapp.data.KMMPreference
import com.kukhtoslava.weatherapp.data.repositoriesimpl.*
import com.kukhtoslava.weatherapp.data.source.*
import com.kukhtoslava.weatherapp.domain.repositories.*
import com.kukhtoslava.weatherapp.utils.AppCoroutineDispatchers
import com.kukhtoslava.weatherapp.utils.PermissionsWrapperController
import io.ktor.client.*
import org.kodein.di.*

val applicationDi = LazyDI {
    DI {
        import(applicationModule)
    }
}

val applicationModule = DI.Module(APP_MODULE) {

    bind<HttpClient>() with singleton {
        provideHttpClient()
    }

    bind<AppCoroutineDispatchers>() with singleton {
        provideAppCoroutineDispatchers()
    }

    bind<PlaceApiService>() with singleton {
        PlaceApiServiceImpl(instance(), instance())
    }

    bind<WeatherApiService>() with singleton {
        WeatherApiServiceImpl(instance(), instance())
    }

    bind<PlaceRepository>() with singleton {
        PlaceRepositoryImpl(instance())
    }

    bind<WeatherRepository>() with singleton {
        WeatherRepositoryImpl(instance())
    }

    bind<CacheRepository>() with singleton {
        CacheRepositoryImpl(instance(), instance())
    }

    bind<InMemoryRepository>() with singleton {
        InMemoryRepositoryImpl()
    }

    bind<LocationRepository>() with singleton {
        LocationRepositoryImpl(instance())
    }

    bind<CityDBDatabase>() with singleton {
        provideCache()
    }

    bind<KMMPreference>() with singleton {
        provideKMMPreference()
    }

    bind<AppErrorMapper>() with multiton {
        AppErrorMapperImpl(PlatformAppErrorMapper())
    }

    bind<LocationService>() with singleton {
        provideLocationService()
    }

    bind<PermissionsWrapperController>() with singleton {
        providePermissionsWrapperController()
    }
}
