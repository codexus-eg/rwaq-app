package com.khater.rwaq.presentation.mapper

import com.khater.rwaq.domain.util.InvalidRequestException
import com.khater.rwaq.domain.util.NoNetworkException
import com.khater.rwaq.domain.util.ServerErrorException
import com.khater.rwaq.domain.util.TooManyRequestsException
import com.khater.rwaq.domain.util.UnAuthorizedException
import com.khater.rwaq.presentation.base.DefaultScreenErrorState

fun handleDefaultException(
    exception: Throwable
): DefaultScreenErrorState {
    return when (exception) {
        is NoNetworkException -> DefaultScreenErrorState.NoNetwork
        is UnAuthorizedException -> DefaultScreenErrorState.UnAuthorized
        is ServerErrorException -> DefaultScreenErrorState.ServerError
        is InvalidRequestException -> DefaultScreenErrorState.InvalidRequest(exception.message)
        is TooManyRequestsException -> DefaultScreenErrorState.TooManyRequests
        else -> DefaultScreenErrorState.SomethingWentWrong(exception.message)
    }
}