package com.kukhtoslava.weatherapp

import com.kukhtoslava.weatherapp.domain.models.AppError
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

internal actual class PlatformAppErrorMapper : (Throwable) -> AppError? {
    actual override fun invoke(t: Throwable): AppError? {
        return when (t) {
            is AppError -> t
            is IOException -> when (t) {
                is UnknownHostException, is SocketException -> AppError.ApiException.NetworkException(t)
                is SocketTimeoutException -> AppError.ApiException.TimeoutException(t)
                else -> null
            }
            else -> null
        }
    }
}
