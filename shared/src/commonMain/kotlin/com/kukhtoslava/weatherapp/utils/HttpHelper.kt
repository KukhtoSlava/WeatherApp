package com.kukhtoslava.weatherapp.utils

import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

fun <T : HttpClientEngineConfig> createHttpClient(
    engineFactory: HttpClientEngineFactory<T>,
    block: T.() -> Unit,
): HttpClient = HttpClient(engineFactory) {
    engine(block)

    install(ContentNegotiation) {
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            prettyPrint = true
            isLenient = true
        }
        json(json)
        register(
            ContentType.Text.Plain,
            KotlinxSerializationConverter(json)
        )
    }

    install(Logging) {
        level = LogLevel.ALL
        logger = object : Logger {
            override fun log(message: String) {
                Napier.d(message = message, tag = "[HttpClient]")
            }
        }
    }
}
