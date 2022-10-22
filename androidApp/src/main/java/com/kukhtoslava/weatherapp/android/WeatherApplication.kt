package com.kukhtoslava.weatherapp.android

import android.app.Application
import com.kukhtoslava.weatherapp.appContext
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class WeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
        initLogger()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Napier.base(DebugAntilog())
        }
    }
}
