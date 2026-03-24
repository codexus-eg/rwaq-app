package com.khater.rwaq.presentation.mapper

import com.khater.rwaq.presentation.base.ErrorState
import org.jetbrains.compose.resources.StringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.error_something_went_wrong

fun mapErrorToMessage(error: ErrorState): StringResource {
    return when (error) {
        is ErrorState.AuthenticationError -> mapAuthenticationErrorToMessage(error.errorState)
        is ErrorState.GenericError -> Res.string.error_something_went_wrong
        is ErrorState.DefaultError -> mapDefaultErrorToMessage(error.errorState)
    }
}