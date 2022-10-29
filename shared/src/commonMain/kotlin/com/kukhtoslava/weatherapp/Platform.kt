package com.kukhtoslava.weatherapp

import com.kukhtoslava.weatherapp.data.KMMPreference
import com.kukhtoslava.weatherapp.data.source.LocationService
import com.kukhtoslava.weatherapp.domain.models.AppError
import com.kukhtoslava.weatherapp.utils.AppCoroutineDispatchers
import com.kukhtoslava.weatherapp.utils.PermissionsWrapperController
import io.ktor.client.*

expect fun provideAppCoroutineDispatchers(): AppCoroutineDispatchers

expect fun provideHttpClient(): HttpClient

expect fun provideCache(): CityDBDatabase

expect fun provideKMMPreference(): KMMPreference

expect fun provideLocationService(): LocationService

expect fun providePermissionsWrapperController(): PermissionsWrapperController

internal expect class PlatformAppErrorMapper constructor() : (Throwable) -> AppError? {
    override fun invoke(t: Throwable): AppError?
}

expect class KMMContext
