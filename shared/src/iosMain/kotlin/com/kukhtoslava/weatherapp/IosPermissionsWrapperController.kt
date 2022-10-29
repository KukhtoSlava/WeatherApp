package com.kukhtoslava.weatherapp

import com.kukhtoslava.weatherapp.utils.PermissionsWrapperController
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.ios.PermissionsController

class IosPermissionsWrapperController : PermissionsWrapperController {

    private val permissionsController = PermissionsController()

    override suspend fun providePermission(permission: Permission) {
        permissionsController.providePermission(permission)
    }

    override fun isPermissionGranted(permission: Permission): Boolean {
        return isPermissionGranted(permission)
    }

    override suspend fun getPermissionState(permission: Permission): PermissionState {
        return getPermissionState(permission)
    }
}
