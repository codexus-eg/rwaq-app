package com.khater.rwaq.data.util


import com.khater.rwaq.domain.util.InvalidCredentialsException
import com.khater.rwaq.domain.util.InvalidRequestException
import com.khater.rwaq.domain.util.NoNetworkException
import com.khater.rwaq.domain.util.PhoneNumberAlreadyExistsException
import com.khater.rwaq.domain.util.ServerErrorException
import com.khater.rwaq.domain.util.TooManyRequestsException
import com.khater.rwaq.domain.util.UnAuthorizedException
import com.khater.rwaq.domain.util.UnknownException
import com.khater.rwaq.domain.util.UserIsBlockedException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.http.HttpStatusCode
import io.ktor.util.network.UnresolvedAddressException

suspend fun <T> safeWrapper(block: suspend () -> T): T {
    return try {
        block()
    } catch (e: ClientRequestException) {
        when (e.response.status) {
            HttpStatusCode.Unauthorized -> throw UnAuthorizedException()
            HttpStatusCode.NotFound -> throw InvalidCredentialsException()
            HttpStatusCode.Forbidden -> throw UserIsBlockedException()
            HttpStatusCode.TooManyRequests -> throw TooManyRequestsException()
            HttpStatusCode.BadRequest -> throw InvalidRequestException()
            HttpStatusCode.Conflict -> throw PhoneNumberAlreadyExistsException()
            HttpStatusCode.InternalServerError -> throw ServerErrorException()
            HttpStatusCode.ServiceUnavailable -> throw ServerErrorException()
            else -> throw UnknownException()
        }
    } catch (e: Exception) {
        when (e) {
            is UnresolvedAddressException -> throw NoNetworkException()
            is HttpRequestTimeoutException -> throw NoNetworkException()
            else -> throw e
        }
    }
}
