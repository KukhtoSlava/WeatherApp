package com.kukhtoslava.weatherapp

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kukhtoslava.weatherapp.utils.PermissionsWrapperController
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController

class AndroidPermissionsWrapperController(application: Application) :
    PermissionsWrapperController {

    private val permissionsController = PermissionsController(applicationContext = application)

    init {
        application.registerActivityLifecycleCallbacks(object :
            DefaultActivityLifecycleCallbacks() {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                (activity as? AppCompatActivity)?.let { cActivity ->
                    permissionsController.bind(cActivity.lifecycle, activity.supportFragmentManager)
                }
            }
        })
    }

    override suspend fun providePermission(permission: Permission) {
        permissionsController.providePermission(permission)
    }

    override fun isPermissionGranted(permission: Permission): Boolean {
        return permissionsController.isPermissionGranted(permission)
    }

    override suspend fun getPermissionState(permission: Permission): PermissionState {
        return permissionsController.getPermissionState(permission)
    }

    open inner class DefaultActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit

        override fun onActivityStarted(activity: Activity) = Unit

        override fun onActivityResumed(activity: Activity) = Unit

        override fun onActivityPaused(activity: Activity) = Unit

        override fun onActivityStopped(activity: Activity) = Unit

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

        override fun onActivityDestroyed(activity: Activity) = Unit
    }
}
