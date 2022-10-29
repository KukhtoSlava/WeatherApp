package com.kukhtoslava.weatherapp

import com.kukhtoslava.weatherapp.data.KMMPreference
import com.kukhtoslava.weatherapp.data.source.LocationService
import com.kukhtoslava.weatherapp.utils.AppCoroutineDispatchers
import com.kukhtoslava.weatherapp.utils.PermissionsWrapperController
import com.kukhtoslava.weatherapp.utils.createHttpClient
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import platform.darwin.NSObject

actual fun provideAppCoroutineDispatchers(): AppCoroutineDispatchers {
    return IosAppCoroutineDispatchers()
}

internal class IosAppCoroutineDispatchers : AppCoroutineDispatchers {
    override val main: CoroutineDispatcher get() = Dispatchers.Main
    override val io: CoroutineDispatcher get() = Dispatchers.Default
    override val default: CoroutineDispatcher get() = Dispatchers.Default
    override val unconfined: CoroutineDispatcher get() = Dispatchers.Unconfined
    override val immediateMain: MainCoroutineDispatcher get() = Dispatchers.Main.immediate
}

actual fun provideHttpClient(): HttpClient {
    return createHttpClient(engineFactory = Darwin) {}
}

actual fun provideCache(): CityDBDatabase {
    val driver = NativeSqliteDriver(CityDBDatabase.Schema, "city.db")
    return CityDBDatabase(driver)
}

actual fun provideLocationService(): LocationService {
    return IosLocationService()
}

actual fun providePermissionsWrapperController(): PermissionsWrapperController {
    return IosPermissionsWrapperController()
}

actual fun provideKMMPreference(): KMMPreference {
    return KMMPreference(NSObject())
}

actual typealias KMMContext = NSObject
