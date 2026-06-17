package com.khater.rwaq.data.repository.referral

import com.khater.rwaq.data.dto.referral.ClaimReferralRequestDto
import com.khater.rwaq.data.dto.referral.ClaimReferralResponseDto
import com.khater.rwaq.data.dto.referral.ReferralInfoResponseDto
import com.khater.rwaq.data.dto.referral.ReferralListResponseDto
import com.khater.rwaq.data.dto.referral.toDomain
import com.khater.rwaq.data.util.getJson
import com.khater.rwaq.data.util.postJson
import com.khater.rwaq.data.util.safeWrapper
import com.khater.rwaq.domain.entities.referral.ReferralClaimResult
import com.khater.rwaq.domain.entities.referral.ReferralInfo
import com.khater.rwaq.domain.entities.referral.ReferralListItem
import com.khater.rwaq.domain.repository.referral.ReferralRepository
import io.ktor.client.HttpClient

class ReferralRepositoryImpl(
    private val httpClient: HttpClient,
) : ReferralRepository {

    override suspend fun claimReferral(referralCode: String, deviceId: String): ReferralClaimResult =
        safeWrapper {
            val response: ClaimReferralResponseDto = httpClient.postJson(
                path = CLAIM_ENDPOINT,
                requestDto = ClaimReferralRequestDto(
                    referralCode = referralCode,
                    deviceId = deviceId.ifBlank { null }
                )
            )
            response.toDomain()
        }

    override suspend fun getMyReferralInfo(): ReferralInfo = safeWrapper {
        val response: ReferralInfoResponseDto = httpClient.getJson(ME_ENDPOINT)
        response.data?.toDomain()
            ?: throw Exception("Server returned success but referral data was missing.")
    }

    override suspend fun getReferrals(userId: String): List<ReferralListItem> = safeWrapper {
        val response: ReferralListResponseDto = httpClient.getJson("api/users/$userId/referrals")
        response.data.map { it.toDomain() }
    }

    companion object {
        const val CLAIM_ENDPOINT = "api/referrals/claim"
        const val ME_ENDPOINT = "api/referrals/me"
    }
}
