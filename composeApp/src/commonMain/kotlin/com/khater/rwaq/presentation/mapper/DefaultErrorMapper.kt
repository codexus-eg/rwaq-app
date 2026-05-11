package com.khater.rwaq.presentation.mapper

import com.khater.rwaq.presentation.base.DefaultScreenErrorState
import com.khater.rwaq.presentation.base.DefaultScreenErrorState.*
import org.jetbrains.compose.resources.StringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.error_invalid_credentials
import rwaq.composeapp.generated.resources.error_invalid_request
import rwaq.composeapp.generated.resources.error_no_network
import rwaq.composeapp.generated.resources.error_something_went_wrong
import rwaq.composeapp.generated.resources.error_too_many_requests

internal fun mapDefaultErrorToMessage(error: DefaultScreenErrorState): StringResource {
    return when (error) {
        DefaultScreenErrorState.NoNetwork -> Res.string.error_no_network
        DefaultScreenErrorState.ServerError -> Res.string.error_no_network
        is DefaultScreenErrorState.SomethingWentWrong -> Res.string.error_something_went_wrong
        DefaultScreenErrorState.TooManyRequests -> Res.string.error_too_many_requests
        DefaultScreenErrorState.UnAuthorized -> Res.string.error_invalid_credentials
        is DefaultScreenErrorState.InvalidRequest -> Res.string.error_invalid_request
    }
}