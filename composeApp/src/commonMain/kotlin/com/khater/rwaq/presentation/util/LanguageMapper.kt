package com.khater.rwaq.presentation.util


import org.jetbrains.compose.resources.StringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.profile_language_arabic
import rwaq.composeapp.generated.resources.profile_language_english

fun mapLanguage(iso: String): StringResource {
    return when (iso) {
        "en" -> Res.string.profile_language_english
        "ar" -> Res.string.profile_language_arabic
        else -> Res.string.profile_language_english
    }
}