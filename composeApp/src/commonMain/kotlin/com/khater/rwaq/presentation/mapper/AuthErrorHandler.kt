package com.khater.rwaq.presentation.mapper

import com.khater.rwaq.domain.util.InvalidCredentialsException
import com.khater.rwaq.domain.util.InvalidMobileNumberException
import com.khater.rwaq.domain.util.InvalidPasswordException
import com.khater.rwaq.domain.util.InvalidRequestException
import com.khater.rwaq.domain.util.NoNetworkException
import com.khater.rwaq.domain.util.PhoneNumberAlreadyExistsException
import com.khater.rwaq.domain.util.TooManyRequestsException
import com.khater.rwaq.domain.util.UnAuthorizedException
import com.khater.rwaq.domain.util.UserIsBlockedException
import com.khater.rwaq.presentation.base.AuthenticationErrorState

fun handleAuthException(
    exception: Throwable,
): AuthenticationErrorState {
    return when (exception) {
        is InvalidMobileNumberException -> AuthenticationErrorState.InvalidMobileNumber
        is InvalidPasswordException -> AuthenticationErrorState.InvalidPassword
        is UserIsBlockedException -> AuthenticationErrorState.UserIsBlocked
        is TooManyRequestsException -> AuthenticationErrorState.TooManyRequests
        is NoNetworkException -> AuthenticationErrorState.NoNetwork
        is InvalidCredentialsException -> AuthenticationErrorState.InvalidCredentials
        is PhoneNumberAlreadyExistsException -> AuthenticationErrorState.PhoneNumberAlreadyExists
        is InvalidRequestException -> AuthenticationErrorState.InvalidRequest
        is UnAuthorizedException -> AuthenticationErrorState.InvalidOTP

        else -> AuthenticationErrorState.SomethingWentWrong(exception.message)
    }
}