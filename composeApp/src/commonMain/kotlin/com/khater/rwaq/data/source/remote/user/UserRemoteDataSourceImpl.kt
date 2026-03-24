package com.khater.rwaq.data.source.remote.user

import com.khater.rwaq.data.dto.user.ContactUsRequest
import com.khater.rwaq.data.dto.user.UpdateUserRequestDto
import com.khater.rwaq.data.dto.user.UserResponseDto
import com.khater.rwaq.data.util.getJson
import com.khater.rwaq.data.util.postJson
import com.khater.rwaq.data.util.putEmail
import com.khater.rwaq.data.util.putJson
import com.khater.rwaq.data.util.putPhoneNumber
import com.khater.rwaq.data.util.putUsername
import com.khater.rwaq.data.util.safeWrapper
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
@OptIn(ExperimentalSettingsApi::class)

class UserRemoteDataSourceImpl(
    private val httpClient: HttpClient,
    private val settings: FlowSettings,
) : UserRemoteDataSource {

    override suspend fun sendContactUsMessage(request: ContactUsRequest) {
        safeWrapper {
            httpClient.postJson<ContactUsRequest, Unit>(
                path = CONTACT_US_ENDPOINT,
                requestDto = request
            )
        }
    }
    override suspend fun getUser(): UserResponseDto {
        return safeWrapper {
             httpClient.getJson<UserResponseDto>(GET_USER_ENDPOINT)
        }
    }
    override suspend fun updateUserData(username: String, email: String) {
        safeWrapper {
            httpClient.putJson<UpdateUserRequestDto,Unit>(
                requestDto = UpdateUserRequestDto(
                    username = username,
                    email = email
                ),
                path = GET_USER_ENDPOINT
            )
        }
        saveAuthCredential(username,email)
    }
    private suspend fun saveAuthCredential(username: String, email: String) {
        settings.putUsername(username)
        settings.putEmail(email)
    }

    companion object {
        const val CONTACT_US_ENDPOINT = "api/contact"
        const val GET_USER_ENDPOINT = "api/users/profile" // Replace with your actual endpoint
    }
}