package com.kukhtoslava.weatherapp.di

import com.kukhtoslava.weatherapp.*
import com.kukhtoslava.weatherapp.data.AppErrorMapper
import com.kukhtoslava.weatherapp.data.AppErrorMapperImpl
import com.kukhtoslava.weatherapp.data.KMMPreference
import com.kukhtoslava.weatherapp.data.repositoriesimpl.CacheRepositoryImpl
import com.kukhtoslava.weatherapp.data.repositoriesimpl.InMemoryRepositoryImpl
import com.kukhtoslava.weatherapp.data.repositoriesimpl.PlaceRepositoryImpl
import com.kukhtoslava.weatherapp.data.repositoriesimpl.WeatherRepositoryImpl
import com.kukhtoslava.weatherapp.data.source.PlaceApiService
import com.kukhtoslava.weatherapp.data.source.PlaceApiServiceImpl
import com.kukhtoslava.weatherapp.data.source.WeatherApiService
import com.kukhtoslava.weatherapp.data.source.WeatherApiServiceImpl
import com.kukhtoslava.weatherapp.domain.repositories.CacheRepository
import com.kukhtoslava.weatherapp.domain.repositories.InMemoryRepository
import com.kukhtoslava.weatherapp.domain.repositories.PlaceRepository
import com.kukhtoslava.weatherapp.domain.repositories.WeatherRepository
import com.kukhtoslava.weatherapp.utils.AppCoroutineDispatchers
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

    bind<CityDBDatabase>() with singleton {
        provideCache()
    }

    bind<KMMPreference>() with singleton {
        provideKMMPreference()
    }

    bind<AppErrorMapper>() with multiton {
        AppErrorMapperImpl(PlatformAppErrorMapper())
    }
}
