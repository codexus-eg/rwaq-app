package com.khater.rwaq.di


import com.khater.rwaq.data.repository.authentication.AuthenticationRepositoryImpl.Companion.REFRESH_ACCESS_TOKEN_ENDPOINT
import com.khater.rwaq.data.repository.authentication.AuthenticationRepositoryImpl.Companion.REGISTER_ENDPOINT
import com.khater.rwaq.data.repository.authentication.AuthenticationRepositoryImpl.Companion.REQUEST_FORGET_PASSWORD_OTP_ENDPOINT
import com.khater.rwaq.data.repository.authentication.AuthenticationRepositoryImpl.Companion.REQUEST_OTP_ENDPOINT
import com.khater.rwaq.data.repository.authentication.AuthenticationRepositoryImpl.Companion.RESET_PASSWORD_ENDPOINT
import com.khater.rwaq.data.repository.authentication.AuthenticationRepositoryImpl.Companion.VERIFY_OTP_ENDPOINT
import com.khater.rwaq.data.repository.authentication.AuthenticationRepositoryImpl.Companion.VERIFY_OTP_FOR_FORGET_PASSWORD_ENDPOINT
import com.khater.rwaq.data.util.accessToken
import com.khater.rwaq.data.util.refreshToken
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json

expect val httpClientEngineFactory: HttpClientEngineFactory<HttpClientEngineConfig>

@OptIn(ExperimentalSettingsApi::class)
fun provideHttpClient(
    baseUrl: String,
    settings: FlowSettings,
    acceptedLanguage: String,
    refreshToken: suspend () -> Unit,
): HttpClient {
    return HttpClient(httpClientEngineFactory) {
        defaultRequest {
            url(baseUrl)
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
          //  header("Accept-Language", acceptedLanguage)

        }

        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    explicitNulls = false
                })
        }

        install(Logging) {
            logger = Logger.Companion.SIMPLE
            level = LogLevel.ALL
        }

        install(plugin = Auth) {
            bearer {
                loadTokens {
                    BearerTokens(
                        accessToken = settings.accessToken.first(),
                        refreshToken = settings.refreshToken.first()
                    )

                }
                refreshTokens {
                    refreshToken()
                    BearerTokens(
                        accessToken = settings.accessToken.first(),
                        refreshToken = settings.refreshToken.first()
                    )
                }
                sendWithoutRequest { request ->
                    val path = request.url.encodedPath.removePrefix("/")
                    co.touchlab.kermit.Logger.i { "acac $path" }
                    co.touchlab.kermit.Logger.i { "acac $REFRESH_ACCESS_TOKEN_ENDPOINT" }
                    path !in listOf(
                        LOGIN_ENDPOINT,
                        REFRESH_ACCESS_TOKEN_ENDPOINT,
                        VERIFY_OTP_FOR_FORGET_PASSWORD_ENDPOINT,
                        REGISTER_ENDPOINT,
                        REQUEST_OTP_ENDPOINT,
                        RESET_PASSWORD_ENDPOINT,
                        VERIFY_OTP_ENDPOINT,
                        REQUEST_FORGET_PASSWORD_OTP_ENDPOINT
                    )
                }
            }
        }

        install(HttpTimeout) {
            requestTimeoutMillis = TIME_OUT_INTERVAL_MILLI
            connectTimeoutMillis = TIME_OUT_INTERVAL_MILLI
            socketTimeoutMillis = TIME_OUT_INTERVAL_MILLI
        }

    }
}
private const val LOGIN_ENDPOINT = "login"
private const val TIME_OUT_INTERVAL_MILLI = 15_000L

