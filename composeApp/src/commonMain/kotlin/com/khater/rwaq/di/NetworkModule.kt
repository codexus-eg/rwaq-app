package com.khater.rwaq.di

import com.khater.rwaq.domain.repository.authentication.AuthenticationRepository
import com.khater.rwaq.domain.repository.service.LocalizationService
import com.russhwolf.settings.ExperimentalSettingsApi
import org.koin.dsl.module
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named


@OptIn(ExperimentalSettingsApi::class)
val networkModule = module {
    single(named(CHAT_JSON)) {
        Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        }
    }
    "https://credible-childhood-cod.ngrok-free.dev/"
    "http://192.168.1.4:3013/"
    "https://rwaq-b04b189d2e97.herokuapp.com/"
    "http://192.168.234.127:3013/"
    single<HttpClient> {
        provideHttpClient(
            baseUrl = "https://rwaq-b04b189d2e97.herokuapp.com/",
            settings = get(),
            refreshToken = { get<AuthenticationRepository>().refreshAccessToken() },
            acceptedLanguage = get<LocalizationService>().getCurrentLanguage().iso
        )
    }
}
private const val BASE_URL = "baseUrl"
const val CUSTOM_HTTP_CLIENT = "customHttpClient"
const val MEDIA_CLIENT = "mediaClient"
const val CHAT_JSON = "chatJson"
const val IMAGE_MESSAGE_SENDER = "image_message_sender"
const val TEXT_MESSAGE_SENDER = "text_message_sender"
const val AUDIO_MESSAGE_SENDER = "audio_message_sender"
const val AYAH_MESSAGE_SENDER = "ayah_message_sender"
const val CHAT_COIL_CLIENT = "chat_coil_client"