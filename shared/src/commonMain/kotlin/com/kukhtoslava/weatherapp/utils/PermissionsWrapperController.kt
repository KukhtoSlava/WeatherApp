package com.kukhtoslava.weatherapp.utils

import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState

interface PermissionsWrapperController {

    suspend fun providePermission(permission: Permission)

    fun isPermissionGranted(permission: Permission): Boolean

    suspend fun getPermissionState(permission: Permission): PermissionState
}
