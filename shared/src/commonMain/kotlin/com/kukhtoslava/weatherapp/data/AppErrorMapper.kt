package com.kukhtoslava.weatherapp.data

import com.kukhtoslava.weatherapp.PlatformAppErrorMapper
import com.kukhtoslava.weatherapp.domain.models.AppError
import io.github.aakira.napier.Napier
import io.ktor.client.network.sockets.*
import io.ktor.client.plugins.*
import io.ktor.util.cio.*

interface AppErrorMapper : (Throwable) -> AppError

internal open class AppErrorMapperImpl(
        private val platformAppErrorMapper: PlatformAppErrorMapper,
) : AppErrorMapper {
    override fun invoke(throwable: Throwable): AppError {
        Napier.d("AppErrorMapperImpl.map $throwable")
        platformAppErrorMapper(throwable)?.let {
            Napier.d("platformAppErrorMapper.map -> $it")
            return it
        }

        return when (throwable) {
            is AppError -> throwable
            is ResponseException -> AppError.ApiException.ServerException(
                    statusCode = throwable.response.status.value,
                    cause = throwable
            )
            is HttpRequestTimeoutException,
            is ConnectTimeoutException,
            is SocketTimeoutException -> AppError.ApiException.TimeoutException(throwable)
            is ChannelReadException -> AppError.ApiException.NetworkException(throwable)
            else -> AppError.UnknownException(throwable)
        }
    }
}
