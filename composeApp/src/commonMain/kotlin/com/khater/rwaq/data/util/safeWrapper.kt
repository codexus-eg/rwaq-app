package com.khater.rwaq.data.util


import com.khater.rwaq.domain.util.InvalidCredentialsException
import com.khater.rwaq.domain.util.InsufficientPointsException
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
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

private val errorJson = Json { ignoreUnknownKeys = true }

suspend fun <T> safeWrapper(block: suspend () -> T): T {
    return try {
        block()
    } catch (e: ClientRequestException) {
        val serverError = try {
            val responseBody = e.response.bodyAsText()
            errorJson.parseToJsonElement(responseBody).jsonObject
        } catch (_: Exception) {
            null
        }
        val serverMessage = serverError?.stringValue("message")
        val serverCode = serverError?.stringValue("code")

        when (e.response.status) {
            HttpStatusCode.Unauthorized -> throw UnAuthorizedException()
            HttpStatusCode.NotFound -> throw InvalidCredentialsException()
            HttpStatusCode.Forbidden -> throw UserIsBlockedException()
            HttpStatusCode.TooManyRequests -> throw TooManyRequestsException()
            HttpStatusCode.BadRequest -> {
                if (serverCode == "INSUFFICIENT_POINTS") {
                    throw InsufficientPointsException(
                        message = serverMessage ?: "Insufficient points",
                        requiredPoints = serverError?.intValue("requiredPoints"),
                        availablePoints = serverError?.intValue("availablePoints")
                    )
                }
                throw InvalidRequestException(serverMessage ?: "Invalid request")
            }
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

private fun JsonObject.stringValue(key: String): String? =
    get(key)?.jsonPrimitive?.contentOrNull

private fun JsonObject.intValue(key: String): Int? =
    stringValue(key)?.toDoubleOrNull()?.toInt()
