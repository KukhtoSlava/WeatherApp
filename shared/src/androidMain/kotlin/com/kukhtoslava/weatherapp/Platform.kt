package com.kukhtoslava.weatherapp

import android.app.Application
import android.content.Context
import com.kukhtoslava.weatherapp.data.KMMPreference
import com.kukhtoslava.weatherapp.utils.AppCoroutineDispatchers
import com.kukhtoslava.weatherapp.utils.createHttpClient
import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher

lateinit var appContext: Application

actual fun provideAppCoroutineDispatchers(): AppCoroutineDispatchers {
    return AndroidAppCoroutineDispatchers()
}

internal class AndroidAppCoroutineDispatchers : AppCoroutineDispatchers {
    override val main: CoroutineDispatcher get() = Dispatchers.Main
    override val io: CoroutineDispatcher get() = Dispatchers.IO
    override val default: CoroutineDispatcher get() = Dispatchers.Default
    override val unconfined: CoroutineDispatcher get() = Dispatchers.Unconfined
    override val immediateMain: MainCoroutineDispatcher get() = Dispatchers.Main.immediate
}

actual fun provideHttpClient(): HttpClient {
    return createHttpClient(engineFactory = OkHttp) {}
}

actual fun provideCache(): CityDBDatabase {
    val driver = AndroidSqliteDriver(CityDBDatabase.Schema, appContext, "city.db")
    return CityDBDatabase(driver)
}

actual fun provideKMMPreference(): KMMPreference {
    return KMMPreference(appContext)
}

actual typealias KMMContext = Application
