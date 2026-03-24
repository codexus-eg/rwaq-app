package com.khater.rwaq.presentation.base

sealed interface DefaultScreenErrorState {
    data object TooManyRequests : DefaultScreenErrorState
    data object NoNetwork : DefaultScreenErrorState
    data object UnAuthorized : DefaultScreenErrorState
    data object ServerError : DefaultScreenErrorState
    data object InvalidRequest : DefaultScreenErrorState
    data class SomethingWentWrong(val message: String?) : DefaultScreenErrorState
}