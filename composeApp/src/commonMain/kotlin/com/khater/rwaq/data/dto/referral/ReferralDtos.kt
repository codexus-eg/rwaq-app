package com.khater.rwaq.data.dto.referral

import com.khater.rwaq.domain.entities.referral.ReferralClaimResult
import com.khater.rwaq.domain.entities.referral.ReferralInfo
import com.khater.rwaq.domain.entities.referral.ReferralListItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ---- Claim --------------------------------------------------------------

@Serializable
data class ClaimReferralRequestDto(
    @SerialName("referralCode") val referralCode: String,
    // Soft signal for abuse detection (logged server-side as a referral_event).
    @SerialName("deviceId") val deviceId: String? = null
)

@Serializable
data class ClaimReferralResponseDto(
    @SerialName("success") val success: Boolean = false,
    @SerialName("pointsAwarded") val pointsAwarded: Int = 0,
    @SerialName("alreadyClaimed") val alreadyClaimed: Boolean = false,
    @SerialName("message") val message: String? = null
)

fun ClaimReferralResponseDto.toDomain(): ReferralClaimResult = ReferralClaimResult(
    success = success,
    pointsAwarded = pointsAwarded,
    alreadyClaimed = alreadyClaimed,
    message = message ?: ""
)

// ---- My referral info (GET /api/referrals/me) ---------------------------

@Serializable
data class ReferralInfoResponseDto(
    @SerialName("success") val success: Boolean = false,
    @SerialName("data") val data: ReferralInfoDto? = null
)

@Serializable
data class ReferralInfoDto(
    @SerialName("referralCode") val referralCode: String = "",
    @SerialName("referralLink") val referralLink: String = "",
    @SerialName("referralPoints") val referralPoints: Int = 0,
    @SerialName("totalReferrals") val totalReferrals: Int = 0,
    @SerialName("totalPointsEarned") val totalPointsEarned: Int = 0,
    @SerialName("settings") val settings: ReferralSettingsDto? = null
)

@Serializable
data class ReferralSettingsDto(
    @SerialName("pointsPerReferral") val pointsPerReferral: Int = 0,
    @SerialName("enabled") val enabled: Boolean = true,
    @SerialName("maxReferralsPerUser") val maxReferralsPerUser: Int = 0
)

fun ReferralInfoDto.toDomain(): ReferralInfo = ReferralInfo(
    referralCode = referralCode,
    referralLink = referralLink,
    referralPoints = referralPoints,
    totalReferrals = totalReferrals,
    totalPointsEarned = totalPointsEarned,
    pointsPerReferral = settings?.pointsPerReferral ?: 0,
    enabled = settings?.enabled ?: true
)

// ---- Referral list (GET /api/users/:id/referrals) -----------------------

@Serializable
data class ReferralListResponseDto(
    @SerialName("success") val success: Boolean = false,
    @SerialName("data") val data: List<ReferralListItemDto> = emptyList(),
    @SerialName("totalReferrals") val totalReferrals: Int = 0,
    @SerialName("totalPointsEarned") val totalPointsEarned: Int = 0
)

@Serializable
data class ReferralListItemDto(
    @SerialName("userId") val userId: String = "",
    @SerialName("username") val username: String? = null,
    @SerialName("phone") val phone: String? = null,
    @SerialName("reward") val reward: Int = 0,
    @SerialName("status") val status: String? = null,
    @SerialName("createdAt") val createdAt: String? = null
)

fun ReferralListItemDto.toDomain(): ReferralListItem = ReferralListItem(
    userId = userId,
    username = username ?: "",
    phone = phone ?: "",
    reward = reward,
    status = status ?: "",
    createdAt = createdAt ?: ""
)
