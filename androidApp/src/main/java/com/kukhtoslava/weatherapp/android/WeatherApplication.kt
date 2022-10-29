package com.kukhtoslava.weatherapp.android

import android.app.Application
import com.kukhtoslava.weatherapp.appContext
import com.kukhtoslava.weatherapp.di.applicationDi
import com.kukhtoslava.weatherapp.utils.PermissionsWrapperController
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.kodein.di.instance

class WeatherApplication : Application() {

    private val permissionsWrapperController: PermissionsWrapperController by applicationDi.instance()

    override fun onCreate() {
        super.onCreate()
        appContext = this
        initLogger()
        permissionsWrapperController // TODO Need to initialization this object on start application
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Napier.base(DebugAntilog())
        }
    }
}
