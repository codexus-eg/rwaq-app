package com.khater.rwaq.data.util

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

suspend inline fun <reified T, reified R> HttpClient.putJson(
    path: String,
    requestDto: T,
    headerParams: Map<String, String> = emptyMap(),
 ): R {
    val response = this.put {
        url(path)
        headerParams.forEach { (key, value) ->
            headers.append(key, value)
        }
        contentType(ContentType.Application.Json)
        setBody(requestDto)
    }

    if (response.status != HttpStatusCode.OK) {
        throw ClientRequestException(response, response.body())
    }

    return response.body()
}

suspend fun HttpClient.putEmpty(
    path: String,
) {
    val response = this.put {
        url(path)
    }

    if (response.status != HttpStatusCode.OK) {
        throw ClientRequestException(response, response.body())
    }
}
