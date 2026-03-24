package com.khater.rwaq.presentation.mapper

import com.khater.rwaq.presentation.base.AuthenticationErrorState
import org.jetbrains.compose.resources.StringResource
import rwaq.composeapp.generated.resources.Res.string
import rwaq.composeapp.generated.resources.error_incorrect_password
import rwaq.composeapp.generated.resources.error_invalid_credentials
import rwaq.composeapp.generated.resources.error_invalid_mobile_number
import rwaq.composeapp.generated.resources.error_invalid_otp
import rwaq.composeapp.generated.resources.error_invalid_password
import rwaq.composeapp.generated.resources.error_invalid_request
import rwaq.composeapp.generated.resources.error_no_network
import rwaq.composeapp.generated.resources.error_otp_expired
import rwaq.composeapp.generated.resources.error_phone_exists
import rwaq.composeapp.generated.resources.error_something_went_wrong
import rwaq.composeapp.generated.resources.error_too_many_requests
import rwaq.composeapp.generated.resources.error_user_is_blocked

internal fun mapAuthenticationErrorToMessage(error: AuthenticationErrorState): StringResource {
    return when (error) {
        AuthenticationErrorState.InvalidMobileNumber -> string.error_invalid_mobile_number
        AuthenticationErrorState.InvalidPassword -> string.error_invalid_password
        AuthenticationErrorState.InvalidOTP -> string.error_invalid_otp
        AuthenticationErrorState.UserIsBlocked -> string.error_user_is_blocked
        AuthenticationErrorState.TooManyRequests -> string.error_too_many_requests
        AuthenticationErrorState.OTPExpired -> string.error_otp_expired
        AuthenticationErrorState.PhoneNumberAlreadyExists -> string.error_phone_exists
        AuthenticationErrorState.NoNetwork -> string.error_no_network
        AuthenticationErrorState.InvalidRequest -> string.error_invalid_request
        AuthenticationErrorState.IncorrectPassword -> string.error_incorrect_password
        AuthenticationErrorState.InvalidCredentials -> string.error_invalid_credentials
        is AuthenticationErrorState.SomethingWentWrong -> string.error_something_went_wrong
    }
}