package com.khater.rwaq.presentation.base

sealed interface ErrorState {
    data class AuthenticationError(val errorState: AuthenticationErrorState) : ErrorState
    data class DefaultError(val errorState: DefaultScreenErrorState) : ErrorState
    data class GenericError(val throwable: Throwable) :ErrorState
}