package com.kukhtoslava.weatherapp.domain.models

sealed class AppError : RuntimeException {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)

    sealed class ApiException(cause: Throwable?) : AppError(cause) {

        class NetworkException(cause: Throwable?) : ApiException(cause)

        class ServerException(
            val statusCode: Int,
            cause: Throwable?,
        ) : ApiException(cause)

        class TimeoutException(cause: Throwable?) : ApiException(cause)

        class UnknownException(cause: Throwable?) : ApiException(cause)
    }

    sealed class LocalStorageException(cause: Throwable?) : AppError(cause) {

        class FileException(cause: Throwable?) : LocalStorageException(cause)

        class DatabaseException(cause: Throwable?) : LocalStorageException(cause)

        class UnknownException(cause: Throwable?) : LocalStorageException(cause)
    }

    class UnknownException(cause: Throwable?) : AppError(cause)
}
